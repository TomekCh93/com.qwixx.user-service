package com.example.user.repository

import com.example.user.model.UserParameters
import com.example.utils.BaseResponse

interface UserRepository {
    suspend fun registerUser(params: UserParameters): BaseResponse<Any>
    suspend fun loginUser(email: String, password: String): BaseResponse<Any>
    suspend fun confirmUser(token: String): BaseResponse<Any>
    suspend fun promoteUserToAdmin(token: String): BaseResponse<Any>
    suspend fun resetPassword(token: String): BaseResponse<Any>
    suspend fun setNewPassword(token: String, newPassword: String): BaseResponse<Any>

}