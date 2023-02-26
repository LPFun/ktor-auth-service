package com.dark.auth.common.logic

import com.dark.auth.common.repo.IUserRepo
import com.dark.auth.common.security.hashing.IHashingService
import com.dark.auth.common.security.token.ITokenService
import com.dark.auth.common.security.token.TokenConfig

class ChainSettings(
    val userRepo: IUserRepo = IUserRepo.NONE,
    val hashingService: IHashingService = IHashingService.NONE,
    val tokenService: ITokenService = ITokenService.NONE,
    val tokenConfig: TokenConfig = TokenConfig.NONE,
)