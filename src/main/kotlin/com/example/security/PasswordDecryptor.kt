package com.example.security

import at.favre.lib.crypto.bcrypt.BCrypt

fun verifyPassword(password: String, hashedPassword: String): Boolean {
    val result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword)
    return result.verified
}