package com.dark.auth.transport

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: String? = null
)