package no.elhub.devxp.model

import kotlinx.serialization.Serializable
import no.elhub.devxp.model.Comment.Json.CommentRelationships
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

data class Comment(
    val id: Int,
    val userId: Int,
    val body: String
) {
    // Database Entity
    object Entity : Table("comment") {
        val id = integer("id").autoIncrement()
        val userId = integer("userid")
        val body = text("body")

        override val primaryKey = PrimaryKey(id)
    }

    // ResultRow constructor
    constructor(row: ResultRow) : this(
        row[Entity.id],
        row[Entity.userId],
        row[Entity.body]
    )

    @Serializable
    data class Json(
        val data: List<CommentData>,
        val meta: MetaResponse? = null,
        val links: LinkResponse? = null
    ) {
        // Note: kotlinx.serialization does not support default values for data classes, so type is set explicitly.
        constructor(comments: List<Comment>, selfLink: String) : this(
            data = comments.map { CommentData(it) },
            meta = MetaResponse(),
            links = LinkResponse(selfLink)
        )

        @Serializable
        data class CommentData(
            val id: Int? = null,
            val type: String,
            val attributes: CommentAttributes,
            val relationships: CommentRelationships? = null
        ) {
            constructor(comment: Comment) : this(
                id = comment.id,
                type = "Comment",
                attributes = CommentAttributes(
                    body = comment.body
                ),
                relationships = CommentRelationships(
                    user = CommentUser(
                        data = CommentUserData(
                            id = comment.userId,
                            type = "User"
                        )
                    )
                )
            )
        }

        @Serializable
        data class CommentAttributes(
            val body: String
        )

        @Serializable
        data class CommentRelationships(
            val user: CommentUser
        )

        @Serializable
        data class CommentUser(
            val data: CommentUserData
        )

        @Serializable
        data class CommentUserData(
            val id: Int,
            val type: String
        )
    }
}
