# ktor-auth-jwt-service

Сервис авторизации через jwt токен c использованием алгоритма шифрования HMAC256

Стек:
 - ktor
 - kodein
 - cache2k
 - kotlinx serialization
 - commons-codec
 - mockk
 - juinit5

Модули:
- app - точка запуска приложения
- common - общий модуль, хранит модели, интерфейсы
- repo-inmemory - модуль хранения данных в памяти
- transport - модуль транпортных моделей
- security - модуль генерации и проверки токена
- logic - модуль логики

Переменные среды:  
JWT_SECRET - секрет для шифрования токена