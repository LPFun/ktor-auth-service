package com.dark

import com.dark.auth.common.models.RepoResult
import com.dark.auth.common.models.User
import com.dark.auth.common.repo.IAuthRepo
import com.dark.auth.common.security.hashing.IHashingService
import com.dark.auth.common.security.token.ITokenService
import com.dark.auth.common.security.token.TokenConfig
import com.dark.auth.plugins.configureRouting
import com.dark.auth.plugins.configureSecurity
import com.dark.auth.plugins.configureSerialization
import com.dark.auth.security.JwtTokenService
import com.dark.auth.security.SHA256HashingService
import com.dark.auth.transport.AuthRequest
import com.dark.auth.utils.property
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.ktor.di

@ExtendWith(MockKExtension::class)
class ApplicationJwtAuthTest {

    @MockK
    private lateinit var authRepo: IAuthRepo

    private fun Application.configureTestDi() {
        di {
            bindSingleton {
                TokenConfig(
                    issuer = property("jwt.issuer"),
                    audience = property("jwt.audience"),
                    expiresIn = property("jwt.expiretime").toLong(),
                    secret = property("jwt.jwt-secret")
                )
            }
            bindSingleton<ITokenService> { JwtTokenService() }
            bindSingleton<IHashingService> { SHA256HashingService() }
            bindSingleton { authRepo }
        }
    }

    private fun ApplicationTestBuilder.getTestClient() = createClient {
        install(ContentNegotiation) {
            json()
        }
    }

    private fun testApp( block: suspend ApplicationTestBuilder.() -> Unit) = testApplication {
        environment {
            config = ApplicationConfig("application-test.yaml")
        }
        application {
            configureTestDi()
            configureSerialization()
            configureSecurity()
            configureRouting()
        }
        block()
    }

    @Test
    fun successSignUp() = testApp {
        val user = User(username = "user", password = "password")
        coEvery { authRepo.getUserByUserName(user.username) } returns RepoResult.Success(User.NONE)
        coEvery { authRepo.insertUser(any()) } returns RepoResult.Success(User.NONE)

        val request = AuthRequest(
            username = user.username,
            password = user.password
        )

        val testClient = getTestClient()
        val response = testClient.post("/signup") {
            contentType(ContentType.Application.Json)
            this.setBody(request)
        }
        assertEquals(HttpStatusCode.OK, response.status)

        coVerify { authRepo.getUserByUserName(user.username) }
        coVerify { authRepo.insertUser(any()) }

        confirmVerified(authRepo)
    }
}