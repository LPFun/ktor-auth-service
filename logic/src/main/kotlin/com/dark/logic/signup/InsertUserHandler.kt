package com.dark.logic.signup

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.dark.auth.common.logic.AuthContext
import com.dark.auth.common.logic.failure
import com.dark.auth.common.models.RepoResult

fun ICorAddExecDsl<AuthContext>.insertUser(title: String) {
    worker {
        this.title = title
        on { chainStatus.isRunning() }
        handle {
            if (userRepo.insertUser(user) !is RepoResult.Success) {
                failure(
                    code = 409,
                    message = "Error save user data"
                )
            }
        }
    }
}
