package com.dark.auth.routes

import com.dark.auth.common.models.RepoResult
import com.dark.auth.common.models.User
import com.dark.auth.common.repo.IAuthRepo
import com.dark.auth.common.security.hashing.IHashingService
import com.dark.auth.common.security.hashing.SaltedHash
import com.dark.auth.common.security.token.ITokenService
import com.dark.auth.common.security.token.TokenClaim
import com.dark.auth.common.security.token.TokenConfig
import com.dark.auth.repo.inmemory.dto.UserDto
import com.dark.auth.transport.AuthRequest
import com.dark.auth.transport.AuthResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import java.util.UUID

fun Route.singUp() {
    val hashingService by closestDI().instance<IHashingService>()
    val userRepo by closestDI().instance<IAuthRepo>()
    post("signup") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        // Валидация
        val areFieldsBlank = request.username.isNullOrBlank() || request.password.isNullOrBlank()
        val isPasswordShort = request.password?.let {
            it.length < 8
        } ?: true

        when (val user = userRepo.getUserByUserName(request.username!!)) {
            is RepoResult.Success -> {
                if (user.data != User.NONE) {
                    call.respond(HttpStatusCode.Conflict, "Username is already exists")
                    return@post
                }
            }

            is RepoResult.Error -> {
                call.respond(HttpStatusCode.Conflict, "Error get data by request")
                return@post
            }
        }

        if (areFieldsBlank || isPasswordShort) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(request.password!!)
        val user = User(
            id = UUID.randomUUID().toString(),
            username = request.username!!,
            password = saltedHash.hash,
            salt = saltedHash.salt
        )
        val wasAcknowledged = userRepo.insertUser(user) is RepoResult.Success
        if (!wasAcknowledged) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        } else {
            call.respond(HttpStatusCode.OK)
        }
    }
}

fun Route.signIn() {
    val userRepo by closestDI().instance<IAuthRepo>()
    val hashingService by closestDI().instance<IHashingService>()
    val tokenService by closestDI().instance<ITokenService>()
    val tokenConfig by closestDI().instance<TokenConfig>()

    post("signin") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val user = when (val userResult = userRepo.getUserByUserName(request.username!!)) {
            is RepoResult.Success -> {
                if (userResult.data == User.NONE) {
                    call.respond(HttpStatusCode.Conflict)
                    return@post
                }
                userResult.data
            }
            is RepoResult.Error -> {
                call.respond(HttpStatusCode.Conflict, "Error get data by request")
                return@post
            }
        }

        val isValidPassword = hashingService.verify(
            value = request.password!!,
            saltedHash = SaltedHash(
                hash = user.password,
                salt = user.salt
            )
        )

        if (!isValidPassword) {
            call.respond(HttpStatusCode.Conflict, "Incorrect username or password")
            return@post
        }

        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(name = "userId", value = user.id),
            TokenClaim(name = "username", value = user.username),
        )

        call.respond(
            status = HttpStatusCode.OK,
            message = AuthResponse(
                token = token
            )
        )
    }
}

fun Route.secret(){
    authenticate {
        get("/secret") {
            val principal = call.principal<JWTPrincipal>()
            val username = principal!!.payload.getClaim("username").asString()
            val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
            call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
        }
    }
}

