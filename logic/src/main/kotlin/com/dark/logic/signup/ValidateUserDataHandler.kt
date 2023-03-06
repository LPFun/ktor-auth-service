package com.dark.logic.signup

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.dark.auth.common.logic.AuthContext
import com.dark.auth.common.logic.failure

fun ICorAddExecDsl<AuthContext>.validateSignUpUserData(title: String) {
    worker {
        this.title = title
        on {
            chainStatus.isRunning()
                    && (listOf(
                signUp.userName,
                signUp.password
            ).any { it.isBlank() } || signUp.password.length < 8)
        }
        handle {
            failure(
                code = 409,
                message = "Username of password are not valid"
            )
        }
    }
}
