package com.dark.auth.routes

import com.dark.auth.common.logic.AuthContext
import com.dark.auth.common.logic.EventType
import com.dark.auth.transport.SignInRequest
import com.dark.auth.transport.SignUpRequest
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

fun Route.singUp() {
    val authChain by closestDI().instance<AuthChain>()
    post("signup") {
        val request = call.receiveNullable<SignUpRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val ctx = AuthContext(
            eventType = EventType.SIGN_UP,
            signUp = request.toModel()
        )

        authChain.exec(ctx)

        ctx.authResult.toTr()

        val responseCode = HttpStatusCode.allStatusCodes.find { it.value == ctx.authResult.code } ?: HttpStatusCode.NotFound
        val response = ctx.authResult.toTr()
        call.respond(
            status = responseCode,
            message = response
        )
    }
}

fun Route.signIn() {
    val authChain by closestDI().instance<AuthChain>()
    post("signin") {
        val request = call.receiveNullable<SignInRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }

        val ctx = AuthContext(
            eventType = EventType.SIGN_IN,
            signIn = request.toModel(),
        )

        authChain.exec(ctx)

        val code = ctx.authResult.code
        val responseCode = HttpStatusCode.allStatusCodes.find { it.value == code } ?: HttpStatusCode.NotFound
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

