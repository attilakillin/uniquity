# Include environment variables.
include .env
export $(shell sed 's/=.*//' .env)

# What the base image of the build backend application should be named.
BASE_NAME="uniquity-base"


# Pull/build images and start containers.
all: build start

# Pull and/or build required images.
build:
	podman pull docker.io/mysql:8
	podman build -t $(BASE_NAME) -f base.Dockerfile ./uniquity-backend
	podman build -t uniquity-backend-1 -f add-extras.Dockerfile \
		--build-arg image=$(BASE_NAME) --build-arg username=$(S1_USER) .
	podman build -t uniquity-backend-2 -f add-extras.Dockerfile \
		--build-arg image=$(BASE_NAME) --build-arg username=$(S2_USER) .

# Create and start containers.
start:
	podman network create uniquity-network || true
	podman run -d -p=3306:3306 --name uniquity-db --net uniquity-network \
		-e MYSQL_DATABASE=$(DB_DATABASE) \
		-e MYSQL_ROOT_PASSWORD=$(DB_ROOT_PASSWORD) \
		-e MYSQL_USER=$(DB_USER) \
		-e MYSQL_PASSWORD=$(DB_PASS) \
		docker.io/mysql:8
	
	@echo Waiting until the database is ready to accept connections...
	while ! (podman exec -it uniquity-db mysqladmin ping --protocol tcp > /dev/null); do sleep 3; done
	
	podman run -d -p=8080:8080 --name uniquity-server-1 --net uniquity-network \
		-e DB_URL=jdbc:mysql://uniquity-db:3306/$(DB_DATABASE) \
		-e DB_USER=$(DB_USER) \
		-e DB_PASS=$(DB_PASS) \
		-e ROOT_FOLDER=$(S1_ROOT_FOLDER) \
		uniquity-backend-1
	podman run -d -p=8081:8080 --name uniquity-server-2 --net uniquity-network \
		-e DB_URL=jdbc:mysql://uniquity-db:3306/$(DB_DATABASE) \
		-e DB_USER=$(DB_USER) \
		-e DB_PASS=$(DB_PASS) \
		-e ROOT_FOLDER=$(S2_ROOT_FOLDER) \
		uniquity-backend-2

	@echo Waiting until both server instances are ready...
	while ! (curl --fail --silent localhost:8080/actuator/health | grep UP || exit 1); do sleep 3; done
	while ! (curl --fail --silent localhost:8081/actuator/health | grep UP || exit 1); do sleep 3; done

# Stop and remove containers. Does not remove images.
stop:
	podman stop uniquity-server-1 || true
	podman stop uniquity-server-2 || true
	podman stop uniquity-db || true
	podman rm uniquity-server-1 || true
	podman rm uniquity-server-2 || true
	podman rm uniquity-db || true
	podman network rm uniquity-network || true

# Stop and remove containers, then delete images as well.
clean: stop
	podman rmi uniquity-backend-1 || true
	podman rmi uniquity-backend-2 || true
	podman rmi uniquity-base || true
	podman rmi docker.io/mysql:8 || true
