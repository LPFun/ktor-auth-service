package com.dark.auth.common.security.token

interface ITokenService {
    fun generate(config: TokenConfig, vararg claims: TokenClaim): String
}