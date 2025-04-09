package no.elhub.devxp.utils

import io.ktor.server.testing.TestApplication
import no.elhub.devxp.config.configureDatabase
import no.elhub.devxp.config.configureKoin
import no.elhub.devxp.config.configureLogging
import no.elhub.devxp.config.configureRouting
import no.elhub.devxp.config.configureSecurity
import no.elhub.devxp.config.configureSerialization

fun defaultTestApplication(): TestApplication = TestApplication {
    application {
        configureDatabase()
        configureKoin()
        configureLogging()
        configureSerialization()
        configureSecurity()
        configureRouting()
    }
}
