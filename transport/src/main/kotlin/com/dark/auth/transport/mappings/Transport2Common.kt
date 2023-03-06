package com.dark.auth.transport.mappings

import com.dark.auth.common.models.SignIn
import com.dark.auth.common.models.SignUp
import com.dark.auth.transport.SignInRequest
import com.dark.auth.transport.SignUpRequest

fun SignInRequest.toModel(): SignIn{
    return SignIn(
        userName = username.toModel(),
        password = password.toModel(),
    )
}

fun SignUpRequest.toModel() = SignUp(
    userName = username.toModel(),
    password = password.toModel()
)