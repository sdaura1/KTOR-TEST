package com.foodvendor.repository

import com.foodvendor.database.DBOrderTable
import com.foodvendor.database.DBVendorTable
import com.foodvendor.database.DatabaseManager
import com.foodvendor.entities.Order
import com.foodvendor.entities.OrderDraft
import com.foodvendor.entities.Vendor
import com.foodvendor.entities.VendorDraft
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class SQLOrderRepository(private val db: Database): OrderRepository {

    override fun init()  = transaction(db){
        SchemaUtils.create(DBOrderTable)
    }

    override fun makeOrder(orderDraft: OrderDraft): Order = transaction {
        val id = DBOrderTable.insert {
            it[id] = UUID.randomUUID().toString()
            it[menuItemId] = orderDraft.menuItemId
            it[businessId] = orderDraft.businessId
            it[phone] = orderDraft.phone
            it[address] = orderDraft.address
            it[date] = orderDraft.date
            it[time] = orderDraft.time
            it[payOnDelivery] = orderDraft.payOnDelivery
            it[quantity] = orderDraft.quantity
        } get DBOrderTable.id
        getOrder(id)!!
    }

    override fun getOrders(vendorId: String): List<Order> = transaction(db) {
        DBOrderTable.selectAll().map {
            Order(
                it[DBOrderTable.id],
                it[DBOrderTable.menuItemId],
                it[DBOrderTable.businessId],
                it[DBOrderTable.quantity],
                it[DBOrderTable.address],
                it[DBOrderTable.phone],
                it[DBOrderTable.date],
                it[DBOrderTable.time],
                it[DBOrderTable.payOnDelivery]
            )
        }
    }

    override fun getOrder(id: String): Order? = transaction{
        DBOrderTable.select { DBVendorTable.id eq id }.map {
            Order(
                it[DBOrderTable.id],
                it[DBOrderTable.menuItemId],
                it[DBOrderTable.businessId],
                it[DBOrderTable.quantity],
                it[DBOrderTable.address],
                it[DBOrderTable.phone],
                it[DBOrderTable.date],
                it[DBOrderTable.time],
                it[DBOrderTable.payOnDelivery]
            )
        }.singleOrNull()
    }

    override fun updateOrder(id: String, orderDraft: OrderDraft): Int = transaction{
        val updatedOrder = DBOrderTable.update({ DBOrderTable.id eq id }) {
            it[menuItemId] = orderDraft.menuItemId
            it[businessId] = orderDraft.businessId
            it[phone] = orderDraft.phone
            it[quantity] = orderDraft.quantity
            it[address] = orderDraft.address
            it[date] = orderDraft.date
            it[time] = orderDraft.time
            it[payOnDelivery] = orderDraft.payOnDelivery
        }
        updatedOrder
    }

    override fun removeOrder(id: String): Int = transaction {
        val deleted = DBOrderTable.deleteWhere{ DBOrderTable.id eq id }
        deleted
    }
}