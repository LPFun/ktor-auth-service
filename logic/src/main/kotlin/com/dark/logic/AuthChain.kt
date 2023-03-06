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
import com.dark.logic.signup.checkIfUserExists
import com.dark.logic.signup.createUser
import com.dark.logic.signup.insertUser
import com.dark.logic.signup.validateSignUpUserData

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
                validateSignUpUserData("Валидация данных пользователя")
                checkIfUserExists("Проверка на существование в БД")
                createUser("Создание пользователся на основе входных данных")
                insertUser("Сохранение пользователся")
                prepareResponse("Подготовка ответа для пользователя")
            }
            finishChain("Обработка окончания цепочки")
        }.build()
    }
}






