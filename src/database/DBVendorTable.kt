package com.foodvendor.database

import org.jetbrains.exposed.sql.Table
import org.ktorm.entity.Entity

object DBVendorTable: Table() {
    val id = varchar("id", 64).primaryKey()
    val name = varchar("name", 100)
    val address = varchar("address", 100)
    val phone = varchar("phone", 16)
    val businessName = varchar("businessName", 100)
    val businessAddress = varchar("businessAddress", 100)
    val businessPhone = varchar("businessPhone", 100)
}