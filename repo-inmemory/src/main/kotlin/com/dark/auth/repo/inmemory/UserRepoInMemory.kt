package com.dark.auth.repo.inmemory

import com.dark.auth.common.models.RepoResult
import com.dark.auth.common.models.User
import com.dark.auth.common.repo.IUserRepo
import com.dark.auth.repo.inmemory.dto.UserDto
import com.dark.auth.repo.inmemory.dto.UserDto.Companion.toDto
import org.cache2k.Cache2kBuilder

class UserRepoInMemory: IUserRepo {
    private val cache = object : Cache2kBuilder<String, UserDto>() {}
        .name("authRepoInMemory")
        .eternal(true)
        .entryCapacity(100)
        .build()

    override suspend fun getUserByUserName(userName: String): RepoResult<User> {
        return try {
            val user = cache[userName]?.toModel() ?: User.NONE
            RepoResult.Success(user)
        } catch (th: Throwable) {
            RepoResult.Error(errors = listOf(th.toString()))
        }
    }

    override suspend fun insertUser(user: User): RepoResult<User> {
        return try {
            cache.put(user.username, user.toDto())
            RepoResult.Success(user)
        } catch (th: Throwable){
            RepoResult.Error(listOf(th.toString()))
        }
    }
}

