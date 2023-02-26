package com.dark.logic

import com.crowdproj.kotlin.cor.handlers.worker
import com.crowdproj.kotlin.cor.rootChain
import com.dark.auth.common.logic.*
import com.dark.auth.common.models.RepoResult
import com.dark.auth.common.models.User
import com.dark.logic.common.finishChain
import com.dark.logic.common.initChain
import com.dark.logic.common.onEvent
import com.dark.logic.common.validateEventType
import com.dark.logic.signin.generateToken
import com.dark.logic.signin.getUser
import com.dark.logic.signin.prepareResponse
import com.dark.logic.signin.validateUser
import java.util.*

class AuthChain(
    private val settings: ChainSettings
) {
    suspend fun exec(context: AuthContext) {
        authLogic.exec(context + settings)
    }

    companion object {
        val authLogic = rootChain<AuthContext> {
            initChain("Инициализация цепочки")
            validateEventType("Валидация события")
            onEvent(EventType.SIGN_IN, "Авторизация пользователя") {
                getUser("Получение данных пользователя по имени")
                validateUser("Валидация данных пользователя")
                generateToken("Генерация токена пользователя")
                prepareResponse("Подготовка ответа для пользователя")
            }
            onEvent(EventType.SIGN_UP, "Регистрация пользователя") {
                // Валидация
                worker {
                    on { chainStatus.isRunning() }
                    handle {
                        val areFieldsBlank = signIn.userName.isBlank() || signIn.password.isBlank()
                        val isPasswordShort = signIn.password.length < 8

                        if (areFieldsBlank || isPasswordShort) {
                            failure(
                                code = 409,
                                message = "Username of password are not valid"
                            )
                        }
                    }
                }

                worker {
                    on { chainStatus.isRunning() }
                    handle {
                        when (val user = userRepo.getUserByUserName(signIn.userName)) {
                            is RepoResult.Success -> {
                                if (user.data != User.NONE) {
                                    failure(
                                        code = 409,
                                        message = "Username is already exists"
                                        )
                                }
                            }

                            is RepoResult.Error -> {
                                failure(
                                    code = 409,
                                    message = "Error get data by request"
                                )
                            }
                        }
                    }
                }

                worker {
                    on { chainStatus.isRunning() }
                    handle {
                        val saltedHash = hashingService.generateSaltedHash(signIn.password!!)
                        user = User(
                            id = UUID.randomUUID().toString(),
                            username = signIn.userName,
                            password = saltedHash.hash,
                            salt = saltedHash.salt
                        )
                    }
                }

                worker {
                    on { chainStatus.isRunning() }
                    handle {
                        val wasAcknowledged = userRepo.insertUser(user) is RepoResult.Success
                        if (!wasAcknowledged) {
                            failure(
                                code = 409,
                                message = "Error save user data"
                            )
                        }
                    }
                }
                prepareResponse("Подготовка ответа для пользователя")
            }

            finishChain("Обработка окончания цепочки")
        }.build()
    }
}




