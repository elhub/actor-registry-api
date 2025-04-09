package no.elhub.devxp.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import no.elhub.devxp.utils.collapseJson
import org.jetbrains.exposed.sql.ResultRow

class CommentTest : DescribeSpec({

    describe("The Comment model") {

        it("should correctly assign properties from a ResultRow") {
            val resultRow = ResultRow.createAndFillValues(
                mapOf(
                    Comment.Entity.id to 1,
                    Comment.Entity.userId to 3,
                    Comment.Entity.body to "This is a comment"
                )
            )
            val comment = Comment(resultRow)
            comment.id shouldBe 1
            comment.userId shouldBe 3
            comment.body shouldBe "This is a comment"
        }

        it("should serialize Comment.Response to JSON correctly") {
            val comments = listOf(
                Comment(id = 1, userId = 3, body = "This is a comment"),
                Comment(id = 2, userId = 4, body = "This is another comment")
            )
            val response = Comment.Json(comments, "https://example.com")
            val json = Json.encodeToString(response)

            val expectedJson = """
                {
                    "data": [
                        {
                            "id": 1,
                            "type": "Comment",
                            "attributes": {
                                "body": "This is a comment"
                            },
                            "relationships": {
                                "user": {
                                    "data": {
                                        "id": 3,
                                        "type": "User"
                                    }
                                }
                            }
                        },
                        {
                            "id": 2,
                            "type": "Comment",
                            "attributes": {
                                "body": "This is another comment"
                            },
                            "relationships": {
                                "user": {
                                    "data": {
                                        "id": 4,
                                        "type": "User"
                                    }
                                }
                            }
                        }
                    ],
                    "meta": {
                        "createdAt": "${response.meta?.createdAt}"
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
