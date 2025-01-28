package com.example.db

import com.example.db.model.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {

    // db initialization
    fun init() {
        Database.connect(hikari())
        transaction {
            SchemaUtils.create(UserTable)
        }
    }

    // hikari config
    private fun hikari(): HikariDataSource {
        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://localhost:5432/mydb"
            driverClassName = "org.postgresql.Driver"
            username = "admin"
            password = "secret"
            maximumPoolSize = 10
            minimumIdle = 2
            idleTimeout = 10000
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        return HikariDataSource(config)
    }

   suspend fun <T> dbQuery(block: () -> T): T = withContext(Dispatchers.IO){
        transaction {
            block()
        }
    }
}