package com.foodvendor.repository

import com.foodvendor.database.DBUserTable
import com.foodvendor.entities.User
import com.foodvendor.entities.UserDraft
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class SQLUserRepository(private val db: Database): UserRepository {

    override fun init() = transaction(db){
        SchemaUtils.create(DBUserTable)
    }

    override fun getUser(phone: String, password: String): User = transaction {
        var returnedUser: User? = null
        DBUserTable.select { DBUserTable.phone eq phone }.map {
            returnedUser = if (it[DBUserTable.password] == password){
                User(
                    it[DBUserTable.id],
                    it[DBUserTable.username],
                    it[DBUserTable.name],
                    it[DBUserTable.phone],
                    it[DBUserTable.address]
                )
            }else {
                null
            }
        }.singleOrNull()
        returnedUser!!
    }

    override fun getLoggedInUser(id: String): User = transaction{
        val returnedUser = DBUserTable.select { DBUserTable.id eq id }.map {
            User(
                it[DBUserTable.id],
                it[DBUserTable.username],
                it[DBUserTable.name],
                it[DBUserTable.phone],
                it[DBUserTable.address]
            )
        }.singleOrNull()!!
        returnedUser
    }

    override fun addUser(userDraft: UserDraft): User = transaction {
        DBUserTable.insert {
            it[id] = UUID.randomUUID().toString()
            it[username] = userDraft.username
            it[name] = userDraft.name
            it[password] = userDraft.password
            it[address] = userDraft.address
            it[phone] = userDraft.phone
        } get DBUserTable.id
        getUser(userDraft.phone, userDraft.password)
    }

    override fun deleteUser(id: String): Int = transaction {
        val deleted = DBUserTable.deleteWhere{ DBUserTable.id eq id }
        deleted
    }

    override fun updateUser(id: String, userDraft: UserDraft): Int = transaction {
        DBUserTable.update({ DBUserTable.id eq id }) {
            it[address] = userDraft.address
        }
    }
}