package com.example.user.service

import com.example.user.model.User
import com.example.user.model.UserParameters

interface UserService {
    suspend fun registerUser(params: UserParameters): User?
    suspend fun findUserByEmail(email: String): User?
    suspend fun checkIfUserIsConfirmed(username: String): Boolean
    suspend fun confirmUser(userId: Int)
    suspend fun promoteUser(userName: String)
    suspend fun setPassword(userId: Int, hashedPass: String)
}