package no.elhub.devxp.model

import org.jetbrains.exposed.sql.Table

object CompanyRole : Table("company_role") {
    val companyId = integer("company_id").references(Company.Entity.id)
    val roleId = integer("role_id").references(Role.Entity.id)

    override val primaryKey = PrimaryKey(companyId, roleId)
}
