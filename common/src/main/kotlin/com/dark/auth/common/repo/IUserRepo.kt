package com.dark.auth.common.repo

import com.dark.auth.common.models.RepoResult
import com.dark.auth.common.models.User

interface IUserRepo {
    suspend fun getUserByUserName(userName: String): RepoResult<User>

    suspend fun insertUser(user: User): RepoResult<User>

    companion object {
        val NONE = object : IUserRepo {
            override suspend fun getUserByUserName(userName: String): RepoResult<User> {
                return RepoResult.Error(listOf("Method ${IUserRepo::getUserByUserName.name} is not implemented"))
            }

            override suspend fun insertUser(user: User): RepoResult<User> {
                return RepoResult.Error(listOf("Method ${IUserRepo::insertUser.name} is not implemented"))
            }
        }
    }
}