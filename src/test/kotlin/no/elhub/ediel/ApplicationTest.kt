package no.elhub.ediel

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplication
import no.elhub.ediel.utils.defaultTestApplication

class ApplicationTest : DescribeSpec({
    lateinit var testApp: TestApplication

    beforeTest {
        testApp = defaultTestApplication()
    }

    afterTest {
        testApp.stop()
    }

    describe("Application") {

        it("should respond with OK for an existing endpoint") {
            val response = testApp.client.get("/ping")
            response.status shouldBe HttpStatusCode.OK
        }

        it("should respond with NotFound for an unknown endpoint") {
            val response = testApp.client.get("/unknown")
            response.status shouldBe HttpStatusCode.NotFound
        }

        it("should return /health OK") {
            val response = testApp.client.get("/health")
            response.status shouldBe HttpStatusCode.OK
            response.bodyAsText() shouldBe "OK"
        }
    }
})
