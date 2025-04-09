package no.elhub.ediel.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabase(): HikariDataSource {
    val config = HikariConfig().apply {
        jdbcUrl = "jdbc:postgresql://localhost:5432/postgres"
        driverClassName = "org.postgresql.Driver"
        username = "postgres"
        password = "postgres"
        maximumPoolSize = 3
        schema = "template"
    }
    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)
    return dataSource
}
