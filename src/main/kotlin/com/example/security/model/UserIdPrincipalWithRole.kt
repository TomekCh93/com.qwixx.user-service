package com.example.security.model

import io.ktor.server.auth.*

data class UserIdPrincipalWithRole(val id: String, val role: String) : Principal
