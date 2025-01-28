package com.example.db.model

import com.example.user.model.UserRole
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object UserTable: Table("users") {
    val id = integer("id").autoIncrement()
    val username = varchar("username",256).uniqueIndex()
    val email = varchar("email",256).uniqueIndex()
    val password = text("password")
    val isConfirmed = bool("is_confirmed").default(defaultValue = false)
    val createdAt = datetime("created_at").clientDefault { LocalDateTime.now() }.nullable()
    val role = varchar("role", 16)

    override val primaryKey = PrimaryKey(id)
}