package com.example.user.repository

import com.auth0.jwt.exceptions.JWTVerificationException
import com.example.user.model.User
import com.example.security.JwtConfig
import com.example.security.verifyPassword
import com.example.user.model.UserParameters
import com.example.user.service.UserService
import com.example.utils.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.mail.sendConfirmationMail
import com.example.mail.sendPasswordResetMail
import hash
import org.jetbrains.exposed.exceptions.ExposedSQLException

class UserRepositoryImpl(
    private val userService: UserService
) : UserRepository {
    override suspend fun registerUser(params: UserParameters): BaseResponse<Any> {
        return if (isEmailExists(params.email)) {
            BaseResponse.ErrorResponse(message = "User already exists")
        } else {
            try {
                val user = userService.registerUser(params)
                if (user != null) {
                    val resetToken = JwtConfig.instance.createAccessToken(user)
                    sendConfirmationMail(user, resetToken)
                    BaseResponse.SuccessfulResponse(data = user, message = "Confirmation email send to ${user.email}")
                } else {
                    BaseResponse.ErrorResponse(message = "Error - user not registered")
                }
            } catch (e: ExposedSQLException) {
                BaseResponse.ErrorResponse(message = "Error - username already registered")
            }
        }
    }

    override suspend fun loginUser(email: String, password: String): BaseResponse<Any> {
        val user = userService.findUserByEmail(email)

        return if (user == null) {
            BaseResponse.ErrorResponse(message = "User not found")
        } else {
            if (verifyConfirmation(user) == false) {
                BaseResponse.ErrorResponse(data = user, message = "User not confirmed")
            } else if (verifyPassword(password, user.password)) {
                generateAuthToken(user)
                BaseResponse.SuccessfulResponse(data = user, message = "Login successful")
            } else {
                BaseResponse.ErrorResponse(message = "Invalid password")
            }
        }
    }

    override suspend fun confirmUser(token: String): BaseResponse<Any> {
        return withContext(Dispatchers.IO) {
            try {
                val verifier = JwtConfig.instance.verifier
                val credential = verifier.verify(token)

                val userId = credential.getClaim(JwtConfig.CLAIM).asInt()

                if (userId != null) {
                    confirmUser(userId)
                    BaseResponse.SuccessfulResponse(
                        data = "User confirmed with ID: $userId",
                        message = "Confirmation successful."
                    )
                } else {
                    BaseResponse.ErrorResponse(message = "Invalid token.")
                }
            } catch (e: JWTVerificationException) {
                BaseResponse.ErrorResponse(message = "Token verification failed: ${e.message}")
            } catch (e: Exception) {
                BaseResponse.ErrorResponse(message = "An error occurred: ${e.message}")
            }
        }
    }

    override suspend fun promoteUserToAdmin(email: String): BaseResponse<Any> {
        val user = userService.findUserByEmail(email)

        return if (user == null) {
            BaseResponse.ErrorResponse(message = "User not found")
        } else {
            userService.promoteUser(email)
            BaseResponse.SuccessfulResponse(data = user, message = "Login successful")
        }

    }

    override suspend fun resetPassword(email: String): BaseResponse<Any> {
        val user = userService.findUserByEmail(email)

        return if (user != null) {
            val resetToken = JwtConfig.instance.createAccessToken(user)

            sendPasswordResetMail(user,resetToken)

            BaseResponse.SuccessfulResponse(
                data = null,
                message = "An email was sent with a link to change the password (if related account exists)."
            )
        } else {
            BaseResponse.SuccessfulResponse(
                data = null,
                message = "An email was sent with a link to change the password (if related account exists)."
            )
        }
    }

    override suspend fun setNewPassword(token: String, newPassword: String): BaseResponse<Any> {
        return withContext(Dispatchers.IO) {
            try {
                val verifier = JwtConfig.instance.verifier
                val credential = verifier.verify(token)

                val userId = credential.getClaim(JwtConfig.CLAIM).asInt()

                if (userId != null) {
                    val hashedPass = hash(newPassword)
                    userService.setPassword(userId, hashedPass)
                    BaseResponse.SuccessfulResponse(
                        data = "New password set to user with ID: $userId",
                        message = "Reset of password successful."
                    )
                } else {
                    BaseResponse.ErrorResponse(message = "Invalid token.")
                }
            } catch (e: JWTVerificationException) {
                BaseResponse.ErrorResponse(message = "Token verification failed: ${e.message}")
            } catch (e: Exception) {
                BaseResponse.ErrorResponse(message = "An error occurred: ${e.message}")
            }
        }
    }


    private fun generateAuthToken(user: User) {
        val token = JwtConfig.instance.createAccessToken(user)
        user.authToken = token
    }

    private suspend fun confirmUser(userId: Int) {
        return userService.confirmUser(userId)
    }

    private suspend fun isEmailExists(email: String): Boolean {
        return userService.findUserByEmail(email) != null
    }

    private suspend fun verifyConfirmation(user: User): Any {
        return userService.checkIfUserIsConfirmed(user.username)
    }
}