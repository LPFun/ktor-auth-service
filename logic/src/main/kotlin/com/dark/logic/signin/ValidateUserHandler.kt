package com.dark.logic.signin

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.dark.auth.common.logic.AuthContext
import com.dark.auth.common.logic.AuthError
import com.dark.auth.common.logic.ChainStatuses
import com.dark.auth.common.logic.ErrorLevel
import com.dark.auth.common.security.hashing.SaltedHash

fun ICorAddExecDsl<AuthContext>.validateUser(title: String) = worker {
    this.title = title
    on {
        chainStatus.isRunning() && hashingService.verify(
            value = signIn.password, saltedHash = SaltedHash(
                hash = user.password, salt = user.salt
            )
        ).not()

    }
    handle {
        chainStatus = ChainStatuses.FAILURE
        errors.add(
            AuthError(
                code = 409, level = ErrorLevel.ERROR, message = "Incorrect username or password"
            )
        )
    }
}