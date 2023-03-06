package com.dark.auth.repo.inmemory.di

import com.dark.auth.common.repo.IUserRepo
import com.dark.auth.repo.inmemory.UserRepoInMemory
import org.kodein.di.DI
import org.kodein.di.bindSingleton

val repoInMemoryModule = DI.Module(name = "repoInMemoryModule"){
    bindSingleton<IUserRepo> { UserRepoInMemory() }
}