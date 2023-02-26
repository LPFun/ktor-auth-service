package com.dark.auth.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.dark.auth.common.security.token.ITokenService
import com.dark.auth.common.security.token.TokenClaim
import com.dark.auth.common.security.token.TokenConfig
import java.util.*

class JwtTokenService: ITokenService {
    override fun generate(config: TokenConfig, vararg claims: TokenClaim): String {
        var token = JWT.create()
            .withAudience(config.audience)
            .withIssuer(config.issuer)
            .withExpiresAt(Date(System.currentTimeMillis() + config.expiresIn))
        claims.forEach { claim ->
            token = token.withClaim(claim.name, claim.value)
        }
        return token.sign(Algorithm.HMAC256(config.secret))
    }
}