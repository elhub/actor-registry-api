package no.elhub.ediel.utils

import io.ktor.server.testing.TestApplication
import no.elhub.ediel.config.configureDatabase
import no.elhub.ediel.config.configureKoin
import no.elhub.ediel.config.configureLogging
import no.elhub.ediel.config.configureRouting
import no.elhub.ediel.config.configureSecurity
import no.elhub.ediel.config.configureSerialization

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
