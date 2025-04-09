# System Design

A number of choices have been made in this template; this document will try to explain the reasoning behind them.

## Foundational

### Language

The service is developed in **Kotlin**, because that is the primary language used in Elhub. Kotlin is a modern language that is fully interoperable with Java
which makes it a good choice for a JVM-based project. It has a lot of modern features that make it more expressive and less error-prone than Java.

### Framework

The service is built using **Ktor**, which is a modern asynchronous web framework for Kotlin. Ktor is a light-weight framework that is well-suited to building
microservices that should be simple, performant and easy to maintain, as opposed to Spring Boot which is more geared toward enterprise-level applications. Both
frameworks may have their place in a development ecosystem. Ktor is chosen for this template because we're working with a simple CRUD service.

### Database Application

The service uses **PostgreSQL** as its database. PostgreSQL and OracleDB are the two databases used in Elhub.

**Liquibase** is used for schema evolution. Liquibase is the preferred tool for managing database changes in Elhub.

### Testing

The service is tested using **Kotest**, which is a flexible and powerful testing framework for Kotlin. Kotest is preferred over JUnit because it is
Kotlin-native, supports behavior-driven development (BDD), extensive matchers, and a lot of other features that make testing more expressive and less
error-prone.

### Documentation

OpenAPI and JSON Schema are used for documenting the APIs. OpenAPI is the de-facto standard for documenting REST APIs, and JSON Schema is a standard for
describing the structure of JSON data. The two standards are used together to provide a comprehensive documentation of the service's APIs.

## Dependencies

### Database Code

**HikariCP** is a high-performance JDBC connection pool. It is extremely fast and lightweight, and at the time of writing, it is the de-facto standard for
connection pooling in the Java ecosystem.

**Exposed** is a lightweight SQL library for Kotlin. It is a Kotlin-native alternative to JPA/Hibernate developed by JetBrains.

### Dependency Injection

**Koin** is essentially the default dependency injection framework for Kotlin. It is lightweight and easy to use, and integrates neatly with Ktor.

### Monitoring

**Kotlin-logging** is a logging library for Kotlin. It is a Kotlin-native alternative to SLF4J/Logback and seems to be the most popular of the various logging
frameworks in Kotlin.

**Micrometer** is a metrics collection library for JVM-based applications. It is the de-facto standard for collecting metrics in Ktor.

**Cohort** is a library for carrying out health checks on Ktor applications. It is specifically designed to provide health checks for orchestrators like
Kubernetes, which makes it particularly well-suited to our use case.

### Serialization

Basic **kotlinx-serialization** is used for JSON serialization. It is a Kotlin-native alternative to Jackson and is developed by JetBrains.

JSON:API is a used as the wire protocol for the service. [JSON:API](https://jsonapi.org/) is the standard for building REST APIs previously chosen by the
Share Bear team.
