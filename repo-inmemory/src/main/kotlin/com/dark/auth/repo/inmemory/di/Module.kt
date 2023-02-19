package com.dark.auth.repo.inmemory.di

import com.dark.auth.common.repo.IAuthRepo
import com.dark.auth.repo.inmemory.AuthRepoInMemory
import org.kodein.di.DI
import org.kodein.di.bindSingleton

val repoInMemoryModule = DI.Module(name = "repoInMemoryModule"){
    bindSingleton<IAuthRepo> { AuthRepoInMemory() }
}