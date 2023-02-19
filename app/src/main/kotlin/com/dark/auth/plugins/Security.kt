package com.dark.auth.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.dark.auth.common.security.token.TokenConfig
import com.dark.auth.utils.property
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.configureSecurity() {
    val config by closestDI().instance<TokenConfig>()
    authentication {
            jwt {
                realm = this@configureSecurity.property("jwt.realm")
                verifier(
                    JWT
                        .require(Algorithm.HMAC256(config.secret))
                        .withAudience(config.audience)
                        .withIssuer(config.issuer)
                        .build()
                )
                validate { credential ->
                    if (credential.payload.audience.contains(config.audience)) JWTPrincipal(credential.payload) else null
                }
                challenge { defaultScheme, realm ->
                    call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
                }
            }
        }
}
