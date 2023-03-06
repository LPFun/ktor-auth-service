package com.dark.auth.common.models

data class SignIn(
    val userName: String = "",
    val password: String = ""
) {
    companion object {
        val NONE = SignIn()
    }
}