package com.dark.logic.common

import com.crowdproj.kotlin.cor.handlers.CorChainDsl
import com.crowdproj.kotlin.cor.handlers.worker
import com.dark.auth.common.logic.*

fun CorChainDsl<AuthContext>.validateEventType(title: String) {
    this.title = title
    worker {
        on { eventType == EventType.NONE }
        handle {
            chainStatus = ChainStatuses.FAILURE
            errors.add(
                AuthError(
                    level = ErrorLevel.ERROR,
                    message = "Event Type is not specified"
                )
            )
        }
    }
}