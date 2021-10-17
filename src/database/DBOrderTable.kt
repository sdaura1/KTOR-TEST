package com.foodvendor.database

import com.foodvendor.database.DBOrderTable.primaryKey
import org.jetbrains.exposed.sql.Table

object DBOrderTable: Table() {
    val orderId = varchar("orderId", 64)
    val id = varchar("id", 64).primaryKey()
    val username = varchar("username", 64)
    var menuItemId = varchar("menuItemId", 100)
    var businessId = varchar("businessId", 100)
    var quantity = integer("quantity")
    var address = varchar("address", 100)
    var phone = varchar("phone", 100)
    var date = varchar("date", 100)
    var time = varchar("time", 100)
    var payOnDelivery = bool("payOnLDelivery")
}