package com.dark.logic.signup

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.dark.auth.common.logic.AuthContext
import com.dark.auth.common.models.User
import java.util.*

fun ICorAddExecDsl<AuthContext>.createUser(title: String) {
    worker {
        this.title = title
        on { chainStatus.isRunning() }
        handle {
            val saltedHash = hashingService.generateSaltedHash(signUp.password)
            user = User(
                id = UUID.randomUUID().toString(),
                username = signUp.userName,
                password = saltedHash.hash,
                salt = saltedHash.salt
            )
        }
    }
}