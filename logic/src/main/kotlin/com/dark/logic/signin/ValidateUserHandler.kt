package com.dark.logic.signin

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.dark.auth.common.logic.*
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
        failure(
            code = 409,
            message = "Incorrect username or password"
        )
    }
}