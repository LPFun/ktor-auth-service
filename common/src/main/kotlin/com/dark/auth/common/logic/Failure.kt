package com.dark.auth.common.logic

fun AuthContext.failure(
    code: Int = Int.MIN_VALUE,
    message: String = ""
) {
    chainStatus = ChainStatuses.FAILURE
    errors.add(
        AuthError(
            code = code, level = ErrorLevel.ERROR, message = message
        )
    )
}