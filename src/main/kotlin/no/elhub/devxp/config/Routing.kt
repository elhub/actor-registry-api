package no.elhub.devxp.config

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.http.content.staticFiles
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import no.elhub.devxp.services.company.CompanyService
import no.elhub.devxp.services.company.companyRoutes
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
const val COMPANIES = "$TEMPLATE_API/companies"

fun Application.configureRouting() {
    val pingService by inject<PingService>()
    val templateService by inject<TemplateService>()
    val companyService by inject<CompanyService>()

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
        route(COMPANIES) {
            companyRoutes(companyService)
        }
        get(HEALTH) {
            call.respondText("OK", status = HttpStatusCode.OK)
        }
        swaggerUI(path = OPENAPI, swaggerFile = "openapi.yaml")
        staticFiles("/schemas", File("schemas"))
    }
}
