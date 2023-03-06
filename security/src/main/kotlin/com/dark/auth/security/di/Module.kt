package com.dark.auth.security.di

import com.dark.auth.common.security.hashing.IHashingService
import com.dark.auth.common.security.token.ITokenService
import com.dark.auth.security.JwtTokenService
import com.dark.auth.security.SHA256HashingService
import org.kodein.di.DI
import org.kodein.di.bindSingleton

val authSecurityModule = DI.Module("authSecurityModule"){
    bindSingleton<ITokenService> { JwtTokenService() }
    bindSingleton<IHashingService> { SHA256HashingService() }
}