package com.example.user.service

import com.example.db.DatabaseFactory.dbQuery
import com.example.db.model.UserTable
import com.example.db.model.UserTable.role
import com.example.user.model.User
import com.example.user.model.UserParameters
import com.example.user.model.UserRole
import hash
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement

class UserServiceImpl : UserService {
    override suspend fun registerUser(params: UserParameters): User? {
        var statement: InsertStatement<Number>? = null
        dbQuery {
            statement = UserTable.insert {
                it[email] = params.email
                it[username] = params.username
                it[password] = hash(params.password)
            }
        }

        return rowToUser(statement?.resultedValues?.get(0))

    }

    override suspend fun findUserByEmail(email: String): User? {
        return dbQuery {
            UserTable
                .select { UserTable.email eq email }
                .mapNotNull { rowToUser(it) }
                .singleOrNull()
        }
    }

    override suspend fun checkIfUserIsConfirmed(username: String): Boolean {
        return dbQuery {
            UserTable
                .select { (UserTable.username eq username).and(UserTable.isConfirmed eq true) }
                .singleOrNull() != null
        }
    }

    override suspend fun confirmUser(userId: Int) {
        return dbQuery {
            UserTable.update({ UserTable.id eq userId }) {
                it[isConfirmed] = true
            }
        }
    }

    override suspend fun promoteUser(email: String) {
        return dbQuery {
            UserTable.update({ UserTable.email eq email }) {
                it[role] = UserRole.ADMIN.name;
            }
        }
    }

    override suspend fun setPassword(userId: Int, hashedPass: String) {
        return dbQuery {
            UserTable.update({ UserTable.id eq userId }) {
                it[password] = hashedPass;
            }
        }
    }

    private fun rowToUser(row: ResultRow?): User? {
        return if (row == null) null
        else User(
            id = row[UserTable.id],
            username = row[UserTable.username],
            email = row[UserTable.email],
            createdAt = row[UserTable.createdAt].toString(),
            isConfirmed = row[UserTable.isConfirmed],
            password = row[UserTable.password],
            role = UserRole.valueOf(row[role])
        )
    }
}