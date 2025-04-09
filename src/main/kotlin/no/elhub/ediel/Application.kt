package no.elhub.ediel

import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain
import no.elhub.ediel.config.configureDatabase
import no.elhub.ediel.config.configureKoin
import no.elhub.ediel.config.configureLogging
import no.elhub.ediel.config.configureMonitoring
import no.elhub.ediel.config.configureRouting
import no.elhub.ediel.config.configureSecurity
import no.elhub.ediel.config.configureSerialization

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val dataSource = configureDatabase()
    configureKoin()
    configureLogging()
    configureMonitoring(dataSource)
    configureSerialization()
    configureSecurity()
    configureRouting()
}
