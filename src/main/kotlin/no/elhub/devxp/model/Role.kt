package no.elhub.devxp.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

data class Role(
    val id: Int,
    val name: String
) {
    object Entity : Table("role") {
        val id = integer("id").autoIncrement()
        val name = varchar("name", 255)

        override val primaryKey = PrimaryKey(id)
    }

    constructor(row: ResultRow) : this(
        row[Entity.id],
        row[Entity.name]
    )

    @Serializable
    data class Json(
        val data: List<RoleData>,
        val meta: MetaResponse? = null,
        val links: LinkResponse? = null
    ) {
        // Note: kotlinx.serialization does not support default values for data classes, so type is set explicitly.
        constructor(roles: List<Role>, selfLink: String) : this(
            data = roles.map { RoleData(it) },
            meta = MetaResponse(),
            links = LinkResponse(selfLink)
        )

        @Serializable
        data class RoleData(
            val id: Int? = null,
            val type: String,
            val attributes: RoleAttributes
        ) {
            constructor(role: Role) : this(
                id = role.id,
                type = "Role",
                attributes = RoleAttributes(
                    name = role.name
                )
            )
        }

        @Serializable
        data class RoleAttributes(
            val name: String
        )
    }
}
