package com.dark.auth.plugins

import com.dark.auth.common.logic.ChainSettings
import com.dark.auth.common.security.token.TokenConfig
import com.dark.auth.repo.inmemory.di.repoInMemoryModule
import com.dark.auth.security.di.authSecurityModule
import com.dark.auth.utils.property
import com.dark.logic.di.authChainModule
import io.ktor.server.application.*
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import org.kodein.di.ktor.di


val appModule = DI.Module("appModule"){
    bindSingleton {
        ChainSettings(
            userRepo = instance(),
            hashingService = instance(),
            tokenService = instance(),
            tokenConfig = instance(),
        )
    }
}

fun Application.configureDi() {
    di {
        importAppDiModules(this@di, this@configureDi)
    }
}
fun importAppDiModules(mainBuilder: DI.MainBuilder, application: Application) = with(mainBuilder) {
    bindSingleton {
        TokenConfig(
            issuer = application.property("jwt.issuer"),
            audience = application.property("jwt.audience"),
            expiresIn = application.property("jwt.expiretime").toLong(),
            secret = application.property("jwt.jwt-secret")
        )
    }
    import(appModule)
    import(repoInMemoryModule)
    import(authChainModule)
    import(authSecurityModule)
}
