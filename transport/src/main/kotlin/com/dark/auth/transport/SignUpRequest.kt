package com.dark.auth.transport

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val username: String? = null,
    val password: String? = null,
)