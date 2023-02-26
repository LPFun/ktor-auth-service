package com.dark.logic.signin

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.dark.auth.common.logic.AuthContext
import com.dark.auth.common.security.token.TokenClaim

fun ICorAddExecDsl<AuthContext>.generateToken(title: String) = worker {
    this.title = title
    on { chainStatus.isRunning() }
    handle {
        token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(name = "userId", value = user.id),
            TokenClaim(name = "username", value = user.username),
        )
    }
}