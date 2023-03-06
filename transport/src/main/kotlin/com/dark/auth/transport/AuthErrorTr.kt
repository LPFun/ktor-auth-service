package com.dark.auth.transport

import kotlinx.serialization.Serializable

@Serializable
data class AuthErrorTr(
    val code: Int? = null,
    val message: String? = null,
)