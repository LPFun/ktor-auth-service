package com.dark.logic.common

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.dark.auth.common.logic.AuthContext

fun ICorAddExecDsl<AuthContext>.finishChain(title: String) = worker {
    this.title = title
    handle {
        println("Finish chain with status: $chainStatus${if(errors.isNotEmpty()) " with errors: $errors" else ""}")
    }
}