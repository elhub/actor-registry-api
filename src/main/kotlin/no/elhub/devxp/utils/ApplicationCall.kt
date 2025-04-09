package no.elhub.devxp.utils

import io.ktor.server.application.ApplicationCall
import io.ktor.server.plugins.origin
import io.ktor.server.request.uri

fun ApplicationCall.callUri(): String {
    val hostHeader = this.request.headers["X-Forwarded-Host"] ?: this.request.origin.serverHost
    val port = this.request.headers["X-Forwarded-Port"] ?: this.request.origin.serverPort.toInt()
    return if (port == "") {
        "${this.request.origin.scheme}://$hostHeader${this.request.uri}"
    } else {
        "${this.request.origin.scheme}://$hostHeader:$port${this.request.uri}"
    }
}
