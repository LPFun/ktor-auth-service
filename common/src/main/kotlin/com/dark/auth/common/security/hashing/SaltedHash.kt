package com.dark.auth.common.security.hashing

data class SaltedHash(
    val hash: String,
    val salt: String
)
