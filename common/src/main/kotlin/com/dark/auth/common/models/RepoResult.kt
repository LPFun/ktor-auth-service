package com.dark.auth.common.models

sealed class RepoResult<out T>(
    val errors: List<String>
){
    class Success<T>(
        val data: T,
        errors: List<String> = emptyList()
    ) : RepoResult<T>(
        errors = errors
    )

    class Error(
        errors: List<String>
    ): RepoResult<Nothing>(
        errors = errors
    )
}