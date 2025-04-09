package no.elhub.devxp.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import no.elhub.devxp.utils.collapseJson
import org.jetbrains.exposed.sql.ResultRow

class UserTest : DescribeSpec({

    describe("The User model") {

        it("should correctly assign properties from a ResultRow") {
            val resultRow = ResultRow.createAndFillValues(
                mapOf(
                    User.Entity.id to 3,
                    User.Entity.name to "Ola"
                )
            )
            val user = User(resultRow)
            user.id shouldBe 3
            user.name shouldBe "Ola"
        }

        it("should serialize User.Response to JSON correctly") {
            val users = listOf(
                User(id = 1, name = "Alice"),
                User(id = 2, name = "Bob")
            )
            val response = User.Json(users, "https://example.com")
            val json = Json.encodeToString(response)

            val expectedJson = """
                {
                    "data": [
                        {
                            "id": 1,
                            "type": "User",
                            "attributes": {
                                "name": "Alice"
                            }
                        },
                        {
                            "id": 2,
                            "type": "User",
                            "attributes": {
                                "name": "Bob"
                            }
                        }
                    ],
                    "meta": {
                        "createdAt": "${response.meta.createdAt}"
                    },
                    "links": {
                        "self": "https://example.com"
                    }
                }
            """.trimIndent()

            json shouldBe expectedJson.collapseJson()
        }
    }
})
