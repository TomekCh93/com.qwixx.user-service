package com.example.security.plugin

import com.example.security.JwtConfig
import com.example.security.model.UserIdPrincipalWithRole
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.securityConfig() {
    JwtConfig.initialize("user-service-qwixx")

    install(Authentication) {
        jwt {
            verifier(JwtConfig.instance.verifier)
            validate { credential ->
                val userId = credential.payload.getClaim(JwtConfig.CLAIM).asInt()
                val role = credential.payload.getClaim("role").asString()

                if (userId != null && role != null) {
                    UserIdPrincipalWithRole(userId.toString(), role)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respondText("Invalid or expired token", status = io.ktor.http.HttpStatusCode.Unauthorized)
            }
        }
    }
}