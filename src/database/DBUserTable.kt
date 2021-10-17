package com.foodvendor.database

import org.jetbrains.exposed.sql.Table

object DBUserTable: Table() {
    val id = varchar("id", 64)
    val username = varchar("username", 64)
    val password = varchar("password", 100)
    var name = varchar("name", 100)
    var phone = varchar("phone", 100).primaryKey()
    var address = varchar("address", 100)
}