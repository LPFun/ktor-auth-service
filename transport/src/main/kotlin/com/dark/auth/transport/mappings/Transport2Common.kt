package com.dark.auth.transport.mappings

import com.dark.auth.common.models.SignIn
import com.dark.auth.transport.AuthRequest

fun AuthRequest.toModel(): SignIn{
    return SignIn(
        userName = username.toModel(),
        password = password.toModel(),
    )
}

