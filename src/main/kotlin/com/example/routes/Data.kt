package com.example.routes

import com.example.user.repository.UserRepository
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.dataRoutes(userRepository: UserRepository) {
    routing {
        authenticate {
            get("/data") {
                call.respond("Example data available only for logged in users.")
            }
        }
    }
}
