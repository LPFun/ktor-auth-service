package com.dark.auth.common.security.hashing

interface IHashingService {
    fun generateSaltedHash(value: String, saltLength: Int = 32): SaltedHash

    fun verify(value: String, saltedHash: SaltedHash): Boolean

    companion object{
        val NONE = object : IHashingService{
            override fun generateSaltedHash(value: String, saltLength: Int): SaltedHash {
                TODO("Not yet implemented")
            }

            override fun verify(value: String, saltedHash: SaltedHash): Boolean {
                TODO("Not yet implemented")
            }
        }
    }
}