package com.dark.auth.common.logic

import com.dark.auth.common.models.SignIn
import com.dark.auth.common.models.SignUp
import com.dark.auth.common.models.User
import com.dark.auth.common.repo.IUserRepo
import com.dark.auth.common.security.hashing.IHashingService
import com.dark.auth.common.security.token.ITokenService
import com.dark.auth.common.security.token.TokenConfig

data class AuthContext(
    var userRepo: IUserRepo = IUserRepo.NONE,
    var hashingService: IHashingService = IHashingService.NONE,
    var tokenService: ITokenService = ITokenService.NONE,
    var tokenConfig: TokenConfig = TokenConfig.NONE,

    val eventType: EventType = EventType.NONE,
    var chainStatus: ChainStatuses = ChainStatuses.NONE,
    val errors: MutableList<AuthError> = mutableListOf(),
    var authResult: AuthResult = AuthResult.NONE,

    var token: String = "",
    val signIn: SignIn = SignIn.NONE,
    val signUp: SignUp = SignUp.NONE,
    var user: User = User.NONE,
)

data class AuthResult(
    val code: Int = Int.MIN_VALUE,
    val errors: List<AuthError> = listOf(),
    val token: String = "",
    val message: String = "",
) {
    companion object{
        val NONE = AuthResult()
    }
}

infix operator fun AuthContext.plus(settings: ChainSettings) = apply {
    userRepo = settings.userRepo
    hashingService = settings.hashingService
    tokenService = settings.tokenService
    tokenConfig = settings.tokenConfig
}

