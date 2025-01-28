package com.example.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.user.model.User

class JwtConfig private constructor(secret: String) {
    companion object {
        private const val ISSUER = "user-service-qwixx"
        private const val AUDIENCE = "user-service-qwixx"
        const val CLAIM = "ID"

        lateinit var instance: JwtConfig private set

        fun initialize(secret: String) {
            synchronized(this) {
                if (!this::instance.isInitialized) {
                    instance = JwtConfig(secret)
                }
            }
        }
    }

    private val algorithm = Algorithm.HMAC256(secret)
    val verifier: JWTVerifier = JWT.require(algorithm).withIssuer(ISSUER).withAudience(AUDIENCE).build()

    fun createAccessToken(user: User): String =
        JWT.create()
            .withIssuer(ISSUER)
            .withAudience(AUDIENCE)
            .withClaim(CLAIM, user.id)
            .withClaim("role", user.role.name)
            .withExpiresAt(java.util.Date(System.currentTimeMillis() + 3_600_000)) // 1 h
            .sign(algorithm)


}

