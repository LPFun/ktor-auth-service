package com.dark.logic

import com.dark.auth.common.logic.AuthContext
import com.dark.auth.common.logic.ChainSettings
import com.dark.auth.common.logic.EventType
import com.dark.auth.common.models.RepoResult
import com.dark.auth.common.models.SignIn
import com.dark.auth.common.models.SignUp
import com.dark.auth.common.models.User
import com.dark.auth.common.repo.IUserRepo
import com.dark.auth.common.security.hashing.IHashingService
import com.dark.auth.common.security.hashing.SaltedHash
import com.dark.auth.common.security.token.ITokenService
import com.dark.auth.common.security.token.TokenClaim
import com.dark.auth.common.security.token.TokenConfig
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
class AuthChainTest {

    @MockK
    private lateinit var userRepo: IUserRepo

    @MockK
    private lateinit var hashingService: IHashingService

    @MockK
    private lateinit var tokenService: ITokenService

    private lateinit var tokenConfig: TokenConfig

    private lateinit var chainSettings: ChainSettings

    private lateinit var authChain: AuthChain

    @BeforeEach
    fun tearUp(){
        tokenConfig = TokenConfig(
            issuer = "issuer",
            audience = "audience",
            expiresIn = 1000,
            secret = "secret"
        )
        chainSettings = ChainSettings(
            userRepo = userRepo,
            hashingService = hashingService,
            tokenConfig = tokenConfig,
            tokenService = tokenService
        )
        authChain = AuthChain(chainSettings)
    }

    @Test
    fun `sign in success test`() = runTest {
        val user = User(
            id = "userId",
            username = "user",
            password = "password",
            salt = "salt"
        )
        val saltedHash = SaltedHash(hash = user.password, salt = user.salt)
        val tokenClaims = arrayOf(TokenClaim(name = "userId", value = user.id), TokenClaim(name = "username", value = user.username))

        coEvery { userRepo.getUserByUserName(user.username) } returns RepoResult.Success(user)
        coEvery { hashingService.verify(user.password, saltedHash) } returns true
        coEvery { tokenService.generate(tokenConfig, *tokenClaims) } returns "generated token"

        val ctx = AuthContext(
            eventType = EventType.SIGN_IN,
            signIn = SignIn(
                userName = user.username,
                password = user.password
            )
        )
        authChain.exec(ctx)

        val result = ctx.authResult
        assertEquals(200, result.code)
        assertEquals("generated token", result.token)
        assertEquals("Successful handle request", result.message)

        coVerify(exactly = 1) {
            userRepo.getUserByUserName(user.username)
            hashingService.verify(user.password, saltedHash)
            tokenService.generate(tokenConfig, *tokenClaims)
        }
        confirmVerified(userRepo, hashingService, tokenService)
    }

    @Test
    fun `sign up success test`() = runTest {
        val user = User(
            id = "userId",
            username = "userName",
            password = "password",
            salt = "salt"
        )

        coEvery { userRepo.getUserByUserName(user.username) } returns RepoResult.Success(User.NONE)
        coEvery { hashingService.generateSaltedHash(user.password) } returns SaltedHash(hash = "hash", salt = "salt")
        coEvery { userRepo.insertUser(any()) } coAnswers { RepoResult.Success(firstArg()) }

        val ctx = AuthContext(
            eventType = EventType.SIGN_UP,
            signUp = SignUp(
                userName = user.username,
                password = user.password
            )
        )
        authChain.exec(ctx)

        val result = ctx.authResult
        assertEquals(200, result.code)
        assertEquals("Successful handle request", result.message)

        coVerify(exactly = 1) {
            userRepo.getUserByUserName(user.username)
            hashingService.generateSaltedHash(user.password)
            userRepo.insertUser(any())
            tokenService wasNot Called
        }

        confirmVerified(userRepo, hashingService)
    }
}