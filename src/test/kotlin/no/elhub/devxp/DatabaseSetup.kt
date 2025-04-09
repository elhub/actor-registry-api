package no.elhub.devxp

import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.HostConfig
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.kotest.core.listeners.AfterProjectListener
import io.kotest.core.listeners.BeforeProjectListener
import liquibase.Liquibase
import liquibase.database.DatabaseFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.DirectoryResourceAccessor
import org.jetbrains.exposed.sql.Database
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import java.nio.file.Paths
import java.sql.DriverManager

object DatabaseSetup : BeforeProjectListener, AfterProjectListener {
    private var postgres: PostgreSQLContainer<out PostgreSQLContainer<*>>? = null
    private const val POSTGRES_PORT = 5432

    override suspend fun beforeProject() {
        postgres = PostgreSQLContainer(DockerImageName.parse("postgres:15-alpine"))
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("postgres")
            .withExposedPorts(POSTGRES_PORT)
            .withCreateContainerCmdModifier { cmd ->
                cmd.withHostConfig(HostConfig().withPortBindings(PortBinding(Ports.Binding.bindPort(POSTGRES_PORT), ExposedPort(POSTGRES_PORT))))
            }
        postgres?.start()
        val url = "jdbc:postgresql://localhost:$POSTGRES_PORT/postgres"
        val user = "postgres"
        val pw = "postgres"
        val changeLogFile = "db/db-changelog.yaml"
        DriverManager.getConnection(url, user, pw).use { connection ->
            val database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(JdbcConnection(connection))
            val liquibase = Liquibase(changeLogFile, DirectoryResourceAccessor(Paths.get(System.getProperty("user.dir"))), database)
            liquibase.update("")
        }

        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://localhost:5432/postgres"
            driverClassName = "org.postgresql.Driver"
            username = "postgres"
            password = "postgres"
            maximumPoolSize = 1
            schema = "template"
        }
        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)
    }

    override suspend fun afterProject() {
        postgres?.stop()
    }
}
