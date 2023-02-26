package com.dark.auth.transport.mappings

/**
 * Маппинги из транспортных модей во внутренние модели
 */
fun String?.toModel(): String = this ?: ""

/**
 * Маппинги из внутренних моделей в трансторные модели
 */
fun String.toTr() = takeIf { it.isNotBlank() }

fun Int.toTr() = takeIf { it != Int.MIN_VALUE }