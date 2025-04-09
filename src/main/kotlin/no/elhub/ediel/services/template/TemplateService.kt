package no.elhub.ediel.services.template

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import kotlinx.serialization.json.Json
import no.elhub.ediel.model.Comment
import no.elhub.ediel.model.User
import no.elhub.ediel.utils.ApiError
import no.elhub.ediel.utils.MissingFieldException
import no.elhub.ediel.utils.callUri
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.koin.core.annotation.Single
import no.elhub.ediel.model.Comment.Json as CommentJson

@Single
class TemplateService {

    // Comments
    suspend fun deleteComment(call: ApplicationCall) {
        try {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                throw IllegalArgumentException("Invalid id in the request.")
            }
            var deletedItems = deleteComment(id)
            if (deletedItems == 0) {
                ApiError.return404NotFound(call, NotFoundException("Comment with id $id not found"))
                return
            }
            call.response.status(HttpStatusCode.OK)
        } catch (e: IllegalArgumentException) {
            ApiError.return400BadRequest(call, e)
            return
        }
    }

    suspend fun getComments(call: ApplicationCall) {
        val comments = retrieveComments()
        call.respond(status = HttpStatusCode.OK, message = Comment.Json(comments, call.callUri()))
    }

    suspend fun getComment(call: ApplicationCall) {
        try {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                throw IllegalArgumentException("Invalid id in the request.")
            }
            val comments = retrieveComments(id)
            if (comments.isEmpty()) {
                ApiError.return404NotFound(call, NotFoundException("Comment with id $id not found"))
                return
            }
            call.respond(status = HttpStatusCode.OK, message = Comment.Json(comments, call.callUri()))
        } catch (e: IllegalArgumentException) {
            ApiError.return400BadRequest(call, e)
            return
        }
    }

    suspend fun patchComment(call: ApplicationCall) {
        try {
            val requestBody = call.receive<String>()
            val newComment = Json.decodeFromString<CommentJson>(requestBody)
            val rowsUpdated = updateComment(newComment)
            if (rowsUpdated == 0) {
                val id = (newComment.data.first().id ?: 0)
                ApiError.return404NotFound(call, NotFoundException("Comment with id $id not found"))
                return
            }
            call.respond(status = HttpStatusCode.OK, message = newComment)
        } catch (e: IllegalArgumentException) {
            ApiError.return400BadRequest(call, e)
            return
        }
    }

    suspend fun postComment(call: ApplicationCall) {
        try {
            val requestBody = call.receive<String>()
            val newComment = Json.decodeFromString<Comment.Json>(requestBody)
            addComments(newComment)
            call.respond(status = HttpStatusCode.Created, message = newComment)
        } catch (e: IllegalArgumentException) {
            ApiError.return400BadRequest(call, e)
            return
        } catch (e: MissingFieldException) {
            ApiError.return400BadRequest(call, e)
            return
        }
    }

    private fun retrieveComments(): List<Comment> {
        var comments: List<Comment> = emptyList()
        val result = transaction {
            Comment.Entity
                .selectAll()
                .toList()
        }
        comments = result.map { Comment(it) }
        return comments
    }

    private fun retrieveComments(cId: Int): List<Comment> {
        var comments: List<Comment> = emptyList()
        val result = transaction {
            Comment.Entity
                .selectAll()
                .where {
                    Comment.Entity.id eq cId
                }
                .toList()
        }
        comments = result.map { Comment(it) }
        return comments
    }

    private fun addComments(comment: Comment.Json) {
        transaction {
            Comment.Entity.insert {
                it[userId] = comment.data.first().relationships?.user?.data?.id ?: throw MissingFieldException("user.id")
                it[body] = comment.data.first().attributes.body
            }
        }
    }

    private fun updateComment(comment: Comment.Json): Int {
        var rowsUpdated = 0
        transaction {
            rowsUpdated = Comment.Entity.update({ Comment.Entity.id eq (comment.data.first().id ?: -1) }) {
                it[body] = comment.data.first().attributes.body
            }
        }
        return rowsUpdated
    }

    private fun deleteComment(cId: Int): Int {
        var deletedItems = 0
        transaction {
            deletedItems = Comment.Entity.deleteWhere { id eq cId }
        }
        return deletedItems
    }

    // Users
    suspend fun getUsers(call: ApplicationCall) {
        val users = retrieveUsers()
        call.respond(status = HttpStatusCode.OK, message = User.Json(users, call.callUri()))
    }

    private fun retrieveUsers(): List<User> {
        var users: List<User> = emptyList()
        val result = transaction {
            User.Entity
                .selectAll()
                .toList()
        }
        users = result.map { User(it) }
        return users
    }

}
