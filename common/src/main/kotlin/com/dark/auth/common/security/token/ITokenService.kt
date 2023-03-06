package com.dark.auth.common.security.token

interface ITokenService {

    fun generate(config: TokenConfig, vararg claims: TokenClaim): String

    companion object{
        val NONE = object : ITokenService {
            override fun generate(config: TokenConfig, vararg claims: TokenClaim): String {
                TODO("Not yet implemented")
            }
        }
    }
}