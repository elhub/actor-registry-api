package no.elhub.devxp.services.ping

import org.koin.core.annotation.Single

const val PING_RESPONSE = "pong"

@Single
class PingService {
    fun ping(): String = PING_RESPONSE
}
