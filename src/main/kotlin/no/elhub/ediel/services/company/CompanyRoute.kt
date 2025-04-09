package no.elhub.ediel.services.company

import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.companyRoutes(companyService: CompanyService) {
    get {
        companyService.getCompanies(call)
    }
    patch {
        companyService.patchCompany(call)
    }
    post {
        companyService.postCompany(call)
    }
    route("/{id}") {
        delete {
            companyService.deleteCompany(call)
        }
        get {
            companyService.getCompany(call)
        }
    }
}
