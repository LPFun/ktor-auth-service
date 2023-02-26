package com.dark.logic.common

import com.crowdproj.kotlin.cor.ICorAddExecDsl
import com.crowdproj.kotlin.cor.handlers.chain
import com.dark.auth.common.logic.AuthContext
import com.dark.auth.common.logic.EventType

fun ICorAddExecDsl<AuthContext>.onEvent(
    eventType: EventType, title: String, block: ICorAddExecDsl<AuthContext>.() -> Unit
) = chain {
    this.title = title
    on { this.eventType == eventType }
    block()
}