package no.elhub.ediel.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

data class User(
    val id: Int,
    val name: String
) {
    object Entity : Table("user") {
        val id = integer("id") // .autoIncrement()
        val name = varchar("name", 255)
        override val primaryKey = PrimaryKey(id)
    }

    constructor(row: ResultRow) : this(
        row[Entity.id],
        row[Entity.name]
    )

    /**
     * Data class for the response of the Users API.
     */
    @Serializable
    data class Json(
        val data: List<UserData>,
        val meta: MetaResponse,
        val links: LinkResponse
    ) {
        // Note: kotlinx.serialization does not support default values for data classes, so type is set explicitly.
        constructor(users: List<User>, selfLink: String) : this (
            data = users.map { UserData(id = it.id, type = "User", attributes = UserAttributes(it.name)) },
            meta = MetaResponse(),
            links = LinkResponse(selfLink)
        )
    }

    @Serializable
    data class UserData(
        val id: Int,
        val type: String,
        val attributes: UserAttributes
    )

    @Serializable
    data class UserAttributes(
        val name: String
    )
}
