package com.dark.logic.di

import com.dark.logic.AuthChain
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val authChainModule = DI.Module("authChainModule"){
    bindSingleton(sync = false) { AuthChain(instance()) }
}