package no.elhub.devxp.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import kotlin.String

data class Company(
    val id: Int,
    val name: String,
    val gln: String,
    val organizationId: String,
    var companyRoles: List<Role> = emptyList()
) {
    fun setRoles(newRoles: List<Role>): Company {
        companyRoles = newRoles
        return this
    }

    // Database Entity
    object Entity : Table("company") {
        val id = integer("id").autoIncrement()
        val name = text("name")
        val gln = text("gln")
        val organizationId = text("organization_id")

        override val primaryKey = PrimaryKey(id)
    }

    // ResultRow constructor
    constructor(row: ResultRow) : this(
        row[Entity.id],
        row[Entity.name],
        row[Entity.gln],
        row[Entity.organizationId]
    )

    @Serializable
    data class Json(
        val data: List<CompanyData>,
        val meta: MetaResponse? = null,
        val links: LinkResponse? = null
    ) {
        // Note: kotlinx.serialization does not support default values for data classes, so type is set explicitly.
        constructor(companies: List<Company>, selfLink: String) : this(
            data = companies.map { CompanyData(it) },
            meta = MetaResponse(),
            links = LinkResponse(selfLink)
        )

        @Serializable
        data class CompanyData(
            val id: Int? = null,
            val type: String,
            val attributes: CompanyAttributes,
            val roles: List<Role.Json.RoleData>? = null
        ) {
            constructor(company: Company) : this(
                id = company.id,
                type = "Company",
                attributes = CompanyAttributes(
                    name = company.name,
                    gln = company.gln,
                    organizationId = company.organizationId
                ),
                roles = company.companyRoles.map { Role.Json.RoleData(it) }
            )
        }

        @Serializable
        data class CompanyAttributes(
            val name: String,
            val gln: String,
            val organizationId: String,
        )
    }
}
