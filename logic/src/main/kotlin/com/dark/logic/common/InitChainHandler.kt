package com.dark.logic.common

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.dark.auth.common.logic.AuthContext
import com.dark.auth.common.logic.ChainStatuses

fun ICorAddExecDsl<AuthContext>.initChain(title: String) = worker {
    this.title = title
    on { chainStatus.isNone() }
    handle {
        chainStatus = ChainStatuses.RUNNING
    }
}