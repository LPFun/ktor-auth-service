package com.dark.auth.transport

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val errors: List<AuthErrorTr>? = null,
    val token: String? = null,
    val message: String? = null,
)