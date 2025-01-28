package com.example

import com.example.db.DatabaseFactory
import com.example.plugins.*
import com.example.routes.adminRoutes
import com.example.user.repository.UserRepositoryImpl
import com.example.routes.authRoutes
import com.example.routes.dataRoutes
import com.example.security.plugin.securityConfig
import com.example.user.service.UserServiceImpl
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DatabaseFactory.init()
    securityConfig()
    configureTemplating()
    configureSerialization()

    val userService = UserServiceImpl()
    val repository = UserRepositoryImpl(userService)

    authRoutes(repository)
    dataRoutes(repository)
    adminRoutes(repository)
}
