package com.dark.auth.utils

import io.ktor.server.application.*

fun Application.property(propertyName: String): String {
    return environment.config.property(propertyName).getString()
}
