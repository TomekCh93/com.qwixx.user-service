package com.example.routes

import com.example.user.model.UserEmailParameter
import com.example.user.repository.UserRepository
import com.example.user.model.UserParameters
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.authRoutes(userRepository: UserRepository) {
    routing {
        route("/auth") {
            post("/register") {
                val params = call.receive<UserParameters>()
                val result = userRepository.registerUser(params)
                call.respond(result.statusCode, result)
            }
            post("/login") {
                val params = call.receive<UserParameters>()
                val result = userRepository.loginUser(params.email, params.password)
                call.respond(result.statusCode, result)
            }

            get("/confirm") {
                val token = call.request.queryParameters["token"]

                if (token != null) {

                    val result = userRepository.confirmUser(token)
                    call.respond(result.statusCode, result)

                } else {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            get("/confirm-password-reset") {
                val token = call.request.queryParameters["token"]

                if (token != null) {
                    val htmlContent = createResetPasswordForm(token)
                    call.respondText(htmlContent, ContentType.Text.Html)
                } else {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            post("/reset-password") {
                val email = call.receive<UserEmailParameter>().email
                val result = userRepository.resetPassword(email)
                call.respond(result.statusCode, result)
            }

            get("/confirm-password-reset") {
                val token = call.request.queryParameters["token"]

                if (token != null) {
                    val htmlContent = createResetPasswordForm(token)
                    call.respondText(htmlContent, ContentType.Text.Html)
                } else {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            post("/set-password") {
                val params = call.receiveParameters()
                val newPassword = params["newPassword"]
                val token = params["token"]
                if (newPassword != null && token != null) {
                    val result = userRepository.setNewPassword(token, newPassword)
                    call.respond(result.statusCode, result)
                } else {
                    call.respond(HttpStatusCode.BadRequest)
                }

            }
        }
    }
}

fun createResetPasswordForm(token: String): String {
    return buildString {
        append("<!DOCTYPE html>")
        append("<html>")
        append("<head><title>Reset Password</title></head>")
        append("<body>")
        append("<h1>Reset Password</h1>")
        append("<form action=\"/auth/set-password\" method=\"post\">")
        append("<input type=\"hidden\" name=\"token\" value=\"$token\"/>")
        append("<label for=\"newPassword\">New Password:</label>")
        append("<input type=\"password\" name=\"newPassword\" required/>")
        append("<br/>")
        append("<input type=\"submit\" value=\"Reset Password\"/>")
        append("</form>")
        append("</body>")
        append("</html>")
    }
}
