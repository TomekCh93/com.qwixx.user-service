package com.example.user.model

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val isConfirmed: Boolean,
    var authToken: String? = null, // for simplicity
    val createdAt: String,
    val password: String,
    val role: UserRole
)

enum class UserRole(val role: String) {
    USER("USER"),
    ADMIN("ADMIN"),
    SUPERADMIN("SUPERADMIN")
}

data class UserParameters(
    val username: String,
    val email: String,
    val password: String
)

data class UserEmailParameter(val email: String)