# Uniquity - A demo Spring Boot application with container deployment

A demo application for querying the list of unique file names in the file system.

The project consists of a Spring Boot application with a REST API interface, and
of a Makefile which can deploy two instances of the backend server, and a shared
database in a Podman configuration.

## Deployment

To build and start the application, simply run `make` in the root of the project. Only `podman` is required
(other than `make`), as the Spring Boot application itself is built as part of the newly created images.

Once the application starts, visit 
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
or 
[http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)
to see the API endpoints of either server instance. You can send requests from here as well.

### Other deployment commands

* Use `make build` to pull and/or build the necessary Docker images.
* Use `make start` to create containers from these images and start them.
* The default goal is `make all`, which is the combination of the above two.
* Use `make stop` to stop the application and remove the containers.
* And use `make clean` to remove the previously created images as well.

## Configuration

Change the values in the `.env` file to influence the behaviour of the application. You can change the database
authentication parameters, as well as the name of the database to use, and you can separately configure each of
the two Spring Boot instances.

### Backend configuration

The following values can be changed for either backend server (each value starts with an `S1_` or `S2_` prefix
respectively):

* `ROOT_FOLDER`: Which folder should the instance use as the root folder of the search queries?
* `USER`: What should the username be of the logged-in user that runs the application in the container?
* `PORT`: Which host port should the instance be connected to?

## Detailed description

The backend application contains two endpoints - one can be used to query files with a given extension
that the container of the running server instance has (under the `ROOT_FOLDER` folder), while the other
can be used to query former requests made to any running server instance.

### Query files

The `/api/unique-names` endpoint can be used to list all unique files present in the preconfigured folder,
or its subfolders,
that have a matching extension to the one provided as a query parameter (`extension`, required - the server
returns an HTTP 400 status code if no value is provided for it).

Sample request:

    http://localhost:8080/api/unique-names?extension=sh

Sample response:

    {
      "count": 2,
      "content": [
        "20locale.sh",
        "00locale.sh"
      ]
    }

## List historical queries

Each server instance persists previous queries in a shared database instance. The other endpoint,
`/api/history` can be used to retrieve a list of all previous queries with the following information:

* Which server instance executed the query?
* When was the query made (in UTC timestamps)?
* Which extension was queried?

Because the database that persists this information is shared between the backend instances, it does not
matter which backend is queried for this historical data, all instances return the same values.

Sample request:

    http://localhost:8080/api/history

Sample response:

    {
      "count": 2,
      "content": [
        {
          "host": "alpha",
          "timestamp": "2023-06-07 18:22:54.377",
          "extension": "sh"
        },
        {
          "host": "beta",
          "timestamp": "2023-06-07 18:28:18.957",
          "extension": "exe"
        }
      ]
    }

## Podman

As an added challenge, Podman was used instead of Docker, and no other orchestration helper tool was
used. The makefile only contains Podman subcommands, and nothing else other than the built-in tools
(and `grep` once to wait until the containers fully initialize).
