package com.foodvendor.database

import org.jetbrains.exposed.sql.Table

object DBOrderTable: Table() {
    val id = varchar("id", 100).primaryKey()
    var menuItemId = varchar("menuItemId", 100)
    var businessId = varchar("businessId", 100)
    var quantity = integer("quantity")
    var address = varchar("address", 100)
    var phone = varchar("phone", 100)
    var date = varchar("date", 100)
    var time = varchar("time", 100)
    var payOnDelivery = bool("payOnLDelivery")
}