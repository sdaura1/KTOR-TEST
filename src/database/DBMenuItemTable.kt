package com.foodvendor.database

import org.jetbrains.exposed.sql.Table

object DBMenuItemTable: Table() {
    val id = varchar("id", 64).primaryKey()
    val businessId = varchar("businessId", 100)
    var name = varchar("name", 100)
    var description = varchar("description", 100)
    var price = double("price")
}