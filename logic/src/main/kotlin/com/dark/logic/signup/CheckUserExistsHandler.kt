package com.dark.logic.signup

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.dark.auth.common.logic.AuthContext
import com.dark.auth.common.logic.failure
import com.dark.auth.common.models.RepoResult
import com.dark.auth.common.models.User

fun ICorAddExecDsl<AuthContext>.checkIfUserExists(title: String) {
    worker {
        this.title = title
        on { chainStatus.isRunning() }
        handle {
            when (val user = userRepo.getUserByUserName(signUp.userName)) {
                is RepoResult.Success -> {
                    if (user.data != User.NONE) {
                        failure(
                            code = 409,
                            message = "Username is already exists"
                        )
                    }
                }

                is RepoResult.Error -> {
                    failure(
                        code = 409,
                        message = "Error get data by request"
                    )
                }
            }
        }
    }
}
