package com.dark.auth.transport

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val username: String? = null,
    val password: String? = null,
)