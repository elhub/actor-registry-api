package no.elhub.devxp.services.template

import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.commentRoutes(templateService: TemplateService) {
    get {
        templateService.getComments(call)
    }
    patch {
        templateService.patchComment(call)
    }
    post {
        templateService.postComment(call)
    }
    route("/{id}") {
        delete {
            templateService.deleteComment(call)
        }
        get {
            templateService.getComment(call)
        }
    }
}

fun Route.userRoutes(templateService: TemplateService) {
    get {
        templateService.getUsers(call)
    }
}
