package com.dark.auth.common.repo

import com.dark.auth.common.models.RepoResult
import com.dark.auth.common.models.User

interface IAuthRepo {
    suspend fun getUserByUserName(userName: String): RepoResult<User>

    suspend fun insertUser(user: User): RepoResult<User>
}