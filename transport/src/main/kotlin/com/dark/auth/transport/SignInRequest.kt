package com.dark.auth.transport

import kotlinx.serialization.Serializable

@Serializable
data class SignInRequest(
    val userId: String? = null,
    val username: String? = null,
    val password: String? = null,
)
