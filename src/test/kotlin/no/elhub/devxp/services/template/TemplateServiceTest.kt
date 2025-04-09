package no.elhub.devxp.services.template

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplication
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import no.elhub.devxp.utils.defaultTestApplication

class TemplateServiceTest : DescribeSpec({
    lateinit var testApp: TestApplication

    beforeTest {
        testApp = defaultTestApplication()
    }

    afterTest {
        testApp.stop()
    }

    describe("Comments API") {

        it("should respond with OK and correct object when GET /comments is called") {
            val response = testApp.client.get("/comments")

            response.status shouldBe HttpStatusCode.OK
            val responseBody = response.bodyAsText()
            val jsonResponse = Json.parseToJsonElement(responseBody).jsonObject

            jsonResponse["data"]?.jsonArray?.size shouldBe 6
            val entry = jsonResponse["data"]?.jsonArray?.get(2)?.jsonObject
            entry shouldNotBe null
            entry?.get("id") shouldBe Json.parseToJsonElement("3")
            entry?.get("attributes")?.jsonObject?.get("body") shouldBe Json
                .parseToJsonElement("\"Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.\"")
        }

        it("should respond with correct comment when GET /comments/{id} is called") {
            val response = testApp.client.get("/comments/3")

            response.status shouldBe HttpStatusCode.OK
            val responseBody = response.bodyAsText()
            val jsonResponse = Json.parseToJsonElement(responseBody).jsonObject

            jsonResponse["data"]?.jsonArray?.size shouldBe 1
            val entry = jsonResponse["data"]?.jsonArray?.get(0)?.jsonObject
            entry shouldNotBe null
            entry?.get("attributes")?.jsonObject?.get("body") shouldBe Json
                .parseToJsonElement("\"Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.\"")
        }

        it("should respond with 404 when GET /comments/{id} is called on non-existent id") {
            val response = testApp.client.get("/comments/999")

            response.status shouldBe HttpStatusCode.NotFound
        }

        it("should respond with 404 when GET /comments/{id} is called on non-integer id") {
            val response = testApp.client.get("/comments/abc")

            response.status shouldBe HttpStatusCode.BadRequest
        }

        it("should respond with correct response when POST /comments is called") {
            val response = testApp.client.post("/comments") {
                setBody(
                    """
                    {
                        "data": [{
                            "type": "Comment",
                            "attributes": {
                                "body": "This is a new comment"
                            }
                            "relationships": {
                                "user": {
                                    "data": {
                                        "type": "User",
                                        "id": 1
                                    }
                                }
                            }
                        }]
                    }
                    """.trimIndent()
                )
            }

            response.status shouldBe HttpStatusCode.Created
            val responseBody = response.bodyAsText()
            val jsonResponse = Json.parseToJsonElement(responseBody).jsonObject

            jsonResponse["data"]?.jsonArray?.size shouldBe 1
            val entry = jsonResponse["data"]?.jsonArray?.get(0)?.jsonObject
            entry shouldNotBe null
            entry?.get("attributes")?.jsonObject?.get("body") shouldBe Json
                .parseToJsonElement("\"This is a new comment\"")
        }

        it("should respond with 400 when POST /comments is called with invalid body") {
            val response = testApp.client.post("/comments") {
                setBody(
                    """
                    {
                        "data": [{
                            "type": "Comment",
                            "attributes": {
                                "body": 123
                            }
                        }]
                    }
                    """.trimIndent()
                )
            }

            response.status shouldBe HttpStatusCode.BadRequest
        }

        it("should respond with correct response when PATCH /comments is called") {
            val response = testApp.client.patch("/comments") {
                setBody(
                    """
                    {
                        "data": [{
                            "id": 1,
                            "type": "Comment",
                            "attributes": {
                                "body": "This is an updated comment"
                            }
                        }]
                    }
                    """.trimIndent()
                )
            }

            response.status shouldBe HttpStatusCode.OK
            val responseBody = response.bodyAsText()
            val jsonResponse = Json.parseToJsonElement(responseBody).jsonObject

            jsonResponse["data"]?.jsonArray?.size shouldBe 1
            val entry = jsonResponse["data"]?.jsonArray?.get(0)?.jsonObject
            entry shouldNotBe null
            entry?.get("attributes")?.jsonObject?.get("body") shouldBe Json
                .parseToJsonElement("\"This is an updated comment\"")
        }

        it("should respond with 400 when PATCH /comments is called with invalid body") {
            val response = testApp.client.patch("/comments") {
                setBody(
                    """
                    {
                        "data": [{
                            "type": "Comment",
                            "attributes": {
                                "body": 123
                            }
                        }]
                    }
                    """.trimIndent()
                )
            }

            response.status shouldBe HttpStatusCode.BadRequest
        }

        it("should respond with 404 when PATCH /comments is called for nonexistent id") {
            val response = testApp.client.patch("/comments") {
                setBody(
                    """
                    {
                        "data": [{
                            "id": 999,
                            "type": "Comment",
                            "attributes": {
                                "body": "This is an updated comment"
                            }
                        }]
                    }
                    """.trimIndent()
                )
            }

            response.status shouldBe HttpStatusCode.NotFound
        }

        it("should respond with 200 when DELETE /comments/{id} is called") {
            val response = testApp.client.delete("/comments/1")

            response.status shouldBe HttpStatusCode.OK
        }

        it("should respond with 404 when DELETE /comments/{id} is called for nonexistent id") {
            val response = testApp.client.delete("/comments/999")

            response.status shouldBe HttpStatusCode.NotFound
        }

        it("should respond with 400 when DELETE /comments/{id} is called for non-integer id") {
            val response = testApp.client.delete("/comments/abc")

            response.status shouldBe HttpStatusCode.BadRequest
        }
    }

    describe("Users API") {

        it("should respond with OK and correct object when GET /ssers is called") {
            val response = testApp.client.get("/users")

            response.status shouldBe HttpStatusCode.OK
            val responseBody = response.bodyAsText()
            val jsonResponse = Json.parseToJsonElement(responseBody).jsonObject

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
                    "links": {
                        "self": "http://localhost:80/users"
                    }
                }
            """.trimIndent()
            val expectedJsonObject = Json.parseToJsonElement(expectedJson).jsonObject

            jsonResponse["data"] shouldBe expectedJsonObject["data"]
            jsonResponse["links"] shouldBe expectedJsonObject["links"]
        }
    }
})
