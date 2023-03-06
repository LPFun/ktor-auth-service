package com.dark.auth.common.security.token

data class TokenConfig(
    val issuer: String = "",
    val audience: String = "",
    val expiresIn: Long = Long.MIN_VALUE,
    val secret: String = ""
) {
    companion object {
        val NONE = TokenConfig()
    }
}