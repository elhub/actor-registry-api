package no.elhub.devxp.services.company

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import kotlinx.serialization.json.Json
import no.elhub.devxp.model.Company
import no.elhub.devxp.utils.ApiError
import no.elhub.devxp.utils.MissingFieldException
import no.elhub.devxp.utils.callUri
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.koin.core.annotation.Single
import no.elhub.devxp.model.Company.Json as CompanyJson

@Single
class CompanyService {

    // Companies
    suspend fun deleteCompany(call: ApplicationCall) {
        try {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                throw IllegalArgumentException("Invalid id in the request.")
            }
            var deletedItems = deleteCompany(id)
            if (deletedItems == 0) {
                ApiError.return404NotFound(call, NotFoundException("Company with id $id not found"))
                return
            }
            call.response.status(HttpStatusCode.OK)
        } catch (e: IllegalArgumentException) {
            ApiError.return400BadRequest(call, e)
            return
        }
    }

    suspend fun getCompanies(call: ApplicationCall) {
        val companies = retrieveCompanies()
        call.respond(status = HttpStatusCode.OK, message = Company.Json(companies, call.callUri()))
    }

    suspend fun getCompany(call: ApplicationCall) {
        try {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                throw IllegalArgumentException("Invalid id in the request.")
            }
            val companies = retrieveCompanies(id)
            if (companies.isEmpty()) {
                ApiError.return404NotFound(call, NotFoundException("Company with id $id not found"))
                return
            }
            call.respond(status = HttpStatusCode.OK, message = Company.Json(companies, call.callUri()))
        } catch (e: IllegalArgumentException) {
            ApiError.return400BadRequest(call, e)
            return
        }
    }

    suspend fun patchCompany(call: ApplicationCall) {
        try {
            val requestBody = call.receive<String>()
            val newCompany = Json.decodeFromString<CompanyJson>(requestBody)
            val rowsUpdated = updateCompany(newCompany)
            if (rowsUpdated == 0) {
                val id = (newCompany.data.first().id ?: 0)
                ApiError.return404NotFound(call, NotFoundException("Company with id $id not found"))
                return
            }
            call.respond(status = HttpStatusCode.OK, message = newCompany)
        } catch (e: IllegalArgumentException) {
            ApiError.return400BadRequest(call, e)
            return
        }
    }

    suspend fun postCompany(call: ApplicationCall) {
        try {
            val requestBody = call.receive<String>()
            val newCompany = Json.decodeFromString<CompanyJson>(requestBody)
            addCompanies(newCompany)
            call.respond(status = HttpStatusCode.Created, message = newCompany)
        } catch (e: IllegalArgumentException) {
            ApiError.return400BadRequest(call, e)
            return
        } catch (e: MissingFieldException) {
            ApiError.return400BadRequest(call, e)
            return
        }
    }

    private fun retrieveCompanies(): List<Company> {
        var companies: List<Company> = emptyList()
        val result = transaction {
            Company.Entity
                .selectAll()
                .toList()
        }
        companies = result.map { Company(it) }
        return companies
    }

    private fun retrieveCompanies(cId: Int): List<Company> {
        var companies: List<Company> = emptyList()
        val result = transaction {
            Company.Entity
                .selectAll()
                .where {
                    Company.Entity.id eq cId
                }
                .toList()
        }
        companies = result.map { Company(it) }
        return companies
    }

    private fun addCompanies(company: CompanyJson) {
        transaction {
            Company.Entity.insert {
                it[name] = company.data.first().attributes.name
                it[gln] = company.data.first().attributes.gln
                it[organizationId] = company.data.first().attributes.organizationId
            }
        }
    }

    private fun updateCompany(company: CompanyJson): Int {
        var rowsUpdated = 0
        transaction {
            rowsUpdated = Company.Entity.update({ Company.Entity.id eq (company.data.first().id ?: -1) }) {
                it[name] = company.data.first().attributes.name
                it[gln] = company.data.first().attributes.gln
                it[organizationId] = company.data.first().attributes.organizationId
            }
        }
        return rowsUpdated
    }

    private fun deleteCompany(cId: Int): Int {
        var deletedItems = 0
        transaction {
            deletedItems = Company.Entity.deleteWhere { id eq cId }
        }
        return deletedItems
    }

}
