package com.dark.auth.common.models

data class User(
    val id: String = "",
    val username: String = "",
    val password: String = "",
    val salt: String = ""
) {
    companion object{
        val NONE = User()
    }
}