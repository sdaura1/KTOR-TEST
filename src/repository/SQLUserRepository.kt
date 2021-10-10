package com.foodvendor.repository

import com.foodvendor.database.DBUserTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class SQLUserRepository(private val db: Database): UserRepository {

    override fun init() = transaction(db){
        SchemaUtils.create(DBUserTable)
    }

    override fun getUser(phone: String, password: String): UserRepository.User? = transaction {
        val returnedUser: UserRepository.User?
        val withUsername = DBUserTable.select { DBUserTable.phone eq phone }.map {
            UserRepository.User(
                it[DBUserTable.id],
                it[DBUserTable.username],
                it[DBUserTable.password],
                it[DBUserTable.name],
                it[DBUserTable.phone],
                it[DBUserTable.address]
            )
        }.singleOrNull()

        returnedUser = if (withUsername?.password == password){
            withUsername
        }else {
            null
        }
        returnedUser
    }

    override fun addUser(user: UserRepository.User): UserRepository.User? = transaction {
        DBUserTable.insert {
            it[id] = UUID.randomUUID().toString()
            it[username] = user.username
            it[name] = user.name
            it[password] = user.password
            it[address] = user.address
            it[phone] = user.phone
        } get DBUserTable.id
        getUser(user.phone, user.password)
    }

    override fun deleteUser(id: String): Int = transaction {
        val deleted = DBUserTable.deleteWhere{ DBUserTable.id eq id }
        deleted
    }

    override fun updateUser(id: String, userDraft: UserRepository.UserDraft): Int = transaction {
        DBUserTable.update({ DBUserTable.id eq id }) {
            it[name] = userDraft.name
            it[phone] = userDraft.phone
            it[address] = userDraft.address
        }
    }
}