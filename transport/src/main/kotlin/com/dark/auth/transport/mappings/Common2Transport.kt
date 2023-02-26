package com.dark.auth.transport.mappings

import com.dark.auth.common.logic.AuthError
import com.dark.auth.common.logic.AuthResult
import com.dark.auth.transport.AuthErrorTr
import com.dark.auth.transport.AuthResponse

fun AuthResult.toTr() = AuthResponse(
    errors = errors.toTr(),
    token = token.toTr(),
    message = message.toTr(),
)

fun List<AuthError>.toTr() = this.map {
    AuthErrorTr(
        code = it.code.toTr(),
        message = it.message.toTr(),
    )
}.takeIf { it.isNotEmpty() }