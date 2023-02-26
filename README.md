# ktor-auth-jwt-service

Сервис авторизации через jwt токен c использованием алгоритма шифрования HMAC256

Стек:
 - ktor
 - kodein
 - cache2k
 - kotlinx serialization
 - mockk

Модули:
- app - точка запуска приложения
- common - общий модуль, хранит модели, интерфейсы
- repo-inmemory - модуль хранения данных в памяти
- transport - модуль транпортных моделей

Переменные среды:  
JWT_SECRET - секрет для шифрования токена