package com.dark.auth.routes

import com.dark.auth.common.logic.AuthContext
import com.dark.auth.common.logic.EventType
import com.dark.auth.common.models.RepoResult
import com.dark.auth.common.models.User
import com.dark.auth.common.repo.IUserRepo
import com.dark.auth.common.security.hashing.IHashingService
import com.dark.auth.common.security.hashing.SaltedHash
import com.dark.auth.common.security.token.ITokenService
import com.dark.auth.common.security.token.TokenClaim
import com.dark.auth.common.security.token.TokenConfig
import com.dark.auth.transport.AuthRequest
import com.dark.auth.transport.AuthResponse
import com.dark.auth.transport.mappings.toModel
import com.dark.auth.transport.mappings.toTr
import com.dark.logic.AuthChain
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
    val userRepo by closestDI().instance<IUserRepo>()
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
    val userRepo by closestDI().instance<IUserRepo>()
    val hashingService by closestDI().instance<IHashingService>()
    val tokenService by closestDI().instance<ITokenService>()
    val tokenConfig by closestDI().instance<TokenConfig>()
    val authChain by closestDI().instance<AuthChain>()

    post("signin") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val signIn = request.toModel()

        val ctx = AuthContext(
            userRepo = userRepo,
            hashingService = hashingService,
            tokenService = tokenService,
            tokenConfig = tokenConfig,
            eventType = EventType.SIGN_IN,
            signIn = signIn,
        )

        authChain.exec(ctx)

        val code = ctx.authResult.code
        val responseCode = HttpStatusCode.allStatusCodes.find { it.value == code.toInt() } ?: HttpStatusCode.NotFound
        val response = ctx.authResult.toTr()
        call.respond(
            status = responseCode,
            message = response
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

