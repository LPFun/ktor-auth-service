package com.dark.auth.common.logic

data class AuthError(
    val code: Int = Int.MIN_VALUE,
    val group: String = "",
    val level: ErrorLevel = ErrorLevel.INFO,
    val message: String = "",
)