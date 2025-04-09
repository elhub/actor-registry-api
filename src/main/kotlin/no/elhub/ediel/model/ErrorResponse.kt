package no.elhub.ediel.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val errors: List<ErrorData>,
    val meta: MetaResponse = MetaResponse()
) {

    @Serializable
    data class ErrorData(
        val status: String,
        val title: String,
        val detail: String
    )
}
