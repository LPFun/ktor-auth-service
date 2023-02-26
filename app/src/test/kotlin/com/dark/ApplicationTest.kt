package com.dark

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        environment {
            config = ApplicationConfig("application-test.yaml")
        }
        client.get("/").apply {
            Assertions.assertEquals(HttpStatusCode.NotFound, status)
        }
    }
}
