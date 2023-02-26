package com.dark.auth.common.logic

enum class ChainStatuses {
    NONE,
    RUNNING,
    FINISH,
    FAILURE;

    fun isRunning() = this == RUNNING

    fun isFailure() = this == FAILURE

    fun isNone() = this == NONE
}