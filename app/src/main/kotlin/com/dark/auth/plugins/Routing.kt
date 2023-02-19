package com.dark.auth.plugins

import com.dark.auth.routes.secret
import com.dark.auth.routes.signIn
import com.dark.auth.routes.singUp
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        singUp()
        signIn()
        secret()
    }
}
