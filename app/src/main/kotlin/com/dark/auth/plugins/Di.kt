package com.dark.auth.plugins

import com.dark.auth.common.security.hashing.IHashingService
import com.dark.auth.common.security.token.ITokenService
import com.dark.auth.common.security.token.TokenConfig
import com.dark.auth.repo.inmemory.di.repoInMemoryModule
import com.dark.auth.security.JwtTokenService
import com.dark.auth.security.SHA256HashingService
import com.dark.auth.utils.property
import io.ktor.server.application.*
import org.kodein.di.bindSingleton
import org.kodein.di.ktor.di

fun Application.configureDi() {
    di {
        bindSingleton {
            TokenConfig(
                issuer = property("jwt.issuer"),
                audience = property("jwt.audience"),
                expiresIn = property("jwt.expiretime").toLong(),
                secret = property("jwt.jwt-secret")
            )
        }
        bindSingleton<ITokenService> { JwtTokenService() }
        bindSingleton<IHashingService> { SHA256HashingService() }
        import(repoInMemoryModule)
    }
}
