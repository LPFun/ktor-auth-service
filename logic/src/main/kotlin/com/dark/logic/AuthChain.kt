package com.dark.logic

import com.crowdproj.kotlin.cor.rootChain
import com.dark.auth.common.logic.AuthContext
import com.dark.auth.common.logic.ChainSettings
import com.dark.auth.common.logic.EventType
import com.dark.auth.common.logic.plus
import com.dark.logic.common.finishChain
import com.dark.logic.common.initChain
import com.dark.logic.common.onEvent
import com.dark.logic.common.validateEventType
import com.dark.logic.signin.generateToken
import com.dark.logic.signin.getUser
import com.dark.logic.signin.prepareResponse
import com.dark.logic.signin.validateUser

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

            }
            finishChain("Обработка окончания цепочки")
        }.build()
    }
}




