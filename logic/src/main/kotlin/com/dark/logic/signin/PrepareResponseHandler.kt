package com.dark.logic.signin

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.dark.auth.common.logic.AuthContext
import com.dark.auth.common.logic.AuthResult

fun ICorAddExecDsl<AuthContext>.prepareResponse(title: String) = worker {
    this.title = title
    handle {
        authResult = AuthResult(
            code = if(chainStatus.isFailure()) errors.firstOrNull()?.code ?: 404 else 200,
            errors = errors,
            token = token,
            message = if(chainStatus.isFailure()) "Error on handle request" else "Successful handle request"
        )
    }
}