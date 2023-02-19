package com.dark.auth.common.security.hashing

interface IHashingService {
    fun generateSaltedHash(value: String, saltLength: Int = 32): SaltedHash

    fun verify(value: String, saltedHash: SaltedHash): Boolean
}