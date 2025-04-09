package no.elhub.devxp.config

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.http.content.staticFiles
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import no.elhub.devxp.services.ping.PingService
import no.elhub.devxp.services.template.TemplateService
import no.elhub.devxp.services.template.commentRoutes
import no.elhub.devxp.services.template.userRoutes
import org.koin.ktor.ext.inject
import java.io.File

const val TEMPLATE_API = ""

const val COMMENTS = "$TEMPLATE_API/comments"
const val HEALTH = "$TEMPLATE_API/health"
const val OPENAPI = "$TEMPLATE_API/openapi"
const val PING = "$TEMPLATE_API/ping"
const val SCHEMAS = "$TEMPLATE_API/schemas"
const val USERS = "$TEMPLATE_API/users"

fun Application.configureRouting() {
    val pingService by inject<PingService>()
    val templateService by inject<TemplateService>()

    routing {
        get(PING) {
            call.respondText(pingService.ping())
        }
        route(COMMENTS) {
            commentRoutes(templateService)
        }
        route(USERS) {
            userRoutes(templateService)
        }
        get(HEALTH) {
            call.respondText("OK", status = HttpStatusCode.OK)
        }
        swaggerUI(path = OPENAPI, swaggerFile = "openapi.yaml")
        staticFiles("/schemas", File("schemas"))
    }
}
