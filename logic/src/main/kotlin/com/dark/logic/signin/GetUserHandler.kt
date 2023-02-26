package com.dark.logic.signin

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.dark.auth.common.logic.AuthContext
import com.dark.auth.common.logic.AuthError
import com.dark.auth.common.logic.ChainStatuses
import com.dark.auth.common.logic.ErrorLevel
import com.dark.auth.common.models.RepoResult
import com.dark.auth.common.models.User

fun ICorAddExecDsl<AuthContext>.getUser(title: String) = worker {
    this.title = title
    description = """
        Если пользователя не существует выдавать ошибку 409.
        Если возникла ошибка при запросе данных пользователя выдавать ошибку 409.
        """.trimIndent()
    on { chainStatus.isRunning() }
    handle {
        user = when (val userResult = userRepo.getUserByUserName(signIn.userName)) {
            is RepoResult.Success -> {
                if (userResult.data == User.NONE) {
                    chainStatus = ChainStatuses.FAILURE
                    errors.add(
                        AuthError(
                            code = 409, level = ErrorLevel.ERROR, message = "User not registered"
                        )
                    )
                    return@handle
                }
                userResult.data
            }

            is RepoResult.Error -> {
                chainStatus = ChainStatuses.FAILURE
                errors += userResult.errors.map {
                    AuthError(
                        code = 409, level = ErrorLevel.ERROR, message = it
                    )
                }
                return@handle
            }
        }
    }
}