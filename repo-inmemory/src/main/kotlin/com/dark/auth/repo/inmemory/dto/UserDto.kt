package com.dark.auth.repo.inmemory.dto

import com.dark.auth.common.models.User

data class UserDto(
    val id: String? = null,
    val username: String? = null,
    val password: String? = null,
    val salt: String? = null,
){
    fun toModel() = User(
        id = id ?: "",
        username = username ?: "",
        password = password ?: "",
        salt = salt ?: ""
    )
    companion object{
        fun User.toDto() = UserDto(
            id = id.takeIfNotBlank(),
            username = username.takeIfNotBlank(),
            password = password.takeIfNotBlank(),
            salt = salt.takeIfNotBlank()
        )
    }
}

fun String.takeIfNotBlank() = takeIf { it.isNotBlank() }