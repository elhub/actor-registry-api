# actor-registry-api

## About

One to two paragraph statement about the project and what it does. This is the minimal documentation required, please
do not copy this template and ignore this.

You should also make sure to update the links in the sections below to point to the correct repository.

## Getting Started

### Prerequisites

List any external prerequisites, dependencies, etc., that are expected on the client side.

### Building & Running

You can build and run the application using gradle.

| Command                       | Description                                                          |
|-------------------------------|----------------------------------------------------------------------|
| `./gradlew run`               | Run the application                                                  |
| `./gradlew build`             | Build everything                                                     |
| `./gradlew test`              | Run the tests                                                        |
| `./gradlew buildFatJar`       | Build an executable JAR of the server with all dependencies included |
| `./gradlew buildImage`        | Build the docker image to use with the fat JAR                       |

After using `./gradlew run` do not forget to take down the database server by running:

```bash
./gradlew databaseComposeDown
```

### Configuration

Modify the application.yaml to adjust server settings or override values using environment variables.

```bash
export PORT=9090
export DATABASE_URL=jdbc:postgresql://localhost:5432/jdbc
```

## API Endpoints

The following endpoints are available:

| Method | Path     | Description             |
|--------|----------|-------------------------|
| GET    | /ping    | Returns pong            |
| GET    | /health  | Returns health info     |
| GET    | /metrics | Returns metrics         |
| GET    | /users  | Returns all users       |
| GET    | /comments  | Returns all comments    |
| POST   | /comments  | Create a comment        |
| PATCH  | /comments  | Update a comment        |
| GET    | /comments/{id} | Returns a comment by id |
| DELETE | /comments/{id} | Delete a comment by id  |

## Contributing

Contributing, issues and feature requests are welcome. See the
[Contributing](https://github.com/elhub/actor-registry-api/blob/main/.github/CONTRIBUTING.md) file.

## Owners

This project is developed by [Elhub](https://www.elhub.no). For the specific development group responsible for this
code, see the [Codeowners](https://github.com/elhub/actor-registry-api/blob/main/.github/CODEOWNERS) file.

## License

This project is proprietary.
