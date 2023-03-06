package com.dark.auth.common.models

data class SignUp(
    val userName: String = "",
    val password: String = ""
) {
    companion object {
        val NONE = SignUp()
    }
}