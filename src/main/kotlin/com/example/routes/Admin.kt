package com.example.routes

import com.example.security.model.UserIdPrincipalWithRole
import com.example.user.model.UserEmailParameter
import com.example.user.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.adminRoutes(userRepository: UserRepository) {
    routing {
        authenticate {
            get("/admin/dashboard") {
                val principal = call.principal<UserIdPrincipalWithRole>()
                if (principal?.role == "ADMIN") {
                    call.respondText("Welcome, Admin!")
                } else {
                    call.respond(HttpStatusCode.Forbidden, "Not Authorized")
                }
            }
            post("/admin/promote") {
                val principal = call.principal<UserIdPrincipalWithRole>()

                if (principal?.role != "ADMIN") {
                    call.respond(HttpStatusCode.Forbidden, "Not Authorized")
                    return@post
                }

                val promoteRequest = call.receive<UserEmailParameter>()

                if (promoteRequest.email.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, "Email parameter is missing or empty.")
                    return@post
                }

                val response = userRepository.promoteUserToAdmin(promoteRequest.email)

                if (response.statusCode.isSuccess()) {
                    call.respond(HttpStatusCode.OK, "User with email: ${promoteRequest.email} promoted to admin.")
                } else {
                    call.respond(HttpStatusCode.NotFound, "User not found or already an admin.")
                }
            }

        }
    }
}
