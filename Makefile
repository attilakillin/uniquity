# Include environment variables.
include .env
export $(shell sed 's/=.*//' .env)

build:
	podman pull docker.io/mysql:8
	podman build -t uniquity-backend ./uniquity-backend

start:
	
	podman network create uniquity-network || true
	podman run -d -p=3306:3306 --name uniquity-db --net uniquity-network \
		-e MYSQL_DATABASE=$(DB_DATABASE) \
		-e MYSQL_ROOT_PASSWORD=$(DB_ROOT_PASSWORD) \
		-e MYSQL_USER=$(DB_USER) \
		-e MYSQL_PASSWORD=$(DB_PASS) \
		docker.io/mysql:8
	while ! (podman logs uniquity-db 2>&1 | grep -q 'ready for connections'); do sleep 3; done
	podman run -d -p=8080:8080 --name uniquity-server-1 --net uniquity-network \
		-e DB_URL=jdbc:mysql://uniquity-db:3306/$(DB_DATABASE) \
		-e DB_USER=$(DB_USER) \
		-e DB_PASS=$(DB_PASS) \
		-e ROOT_FOLDER=$(S1_ROOT_FOLDER) \
		uniquity-backend
	podman run -d -p=8081:8080 --name uniquity-server-2 --net uniquity-network \
		-e DB_URL=jdbc:mysql://uniquity-db:3306/$(DB_DATABASE) \
		-e DB_USER=$(DB_USER) \
		-e DB_PASS=$(DB_PASS) \
		-e ROOT_FOLDER=$(S2_ROOT_FOLDER) \
		uniquity-backend
	while ! (podman logs uniquity-server-1 2>&1 | grep -q 'Started UniquityBackendApplication'); do sleep 3; done
	while ! (podman logs uniquity-server-2 2>&1 | grep -q 'Started UniquityBackendApplication'); do sleep 3; done

stop:
	podman stop uniquity-server-1 || true
	podman stop uniquity-server-2 || true
	podman stop uniquity-db || true
	podman rm uniquity-server-1 || true
	podman rm uniquity-server-2 || true
	podman rm uniquity-db || true

