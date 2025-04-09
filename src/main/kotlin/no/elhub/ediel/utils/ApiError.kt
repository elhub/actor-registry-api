package no.elhub.ediel.utils

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import no.elhub.ediel.model.ErrorResponse
import no.elhub.ediel.model.ErrorResponse.ErrorData

private val logger = KotlinLogging.logger {}

object ApiError {

    suspend fun return400BadRequest(call: ApplicationCall, e: Exception) {
        logger.debug { "ApiError: ${e.localizedMessage}" }
        val error = ErrorData(
            HttpStatusCode.BadRequest.toString(),
            "Invalid argument in request",
            e.message.toString()
        )
        call.respond(
            status = HttpStatusCode.BadRequest,
            message = ErrorResponse(listOf(error))
        )
    }

    suspend fun return404NotFound(call: ApplicationCall, e: Exception) {
        val error = ErrorData(
            HttpStatusCode.NotFound.toString(),
            "Resource not found",
            e.message.toString()
        )
        call.respond(
            status = HttpStatusCode.NotFound,
            message = ErrorResponse(listOf(error))
        )
    }
}
