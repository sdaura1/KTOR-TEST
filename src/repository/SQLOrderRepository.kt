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
import kotlin.collections.ArrayList

class SQLOrderRepository(private val db: Database): OrderRepository {

    override fun init() = transaction(db){
        SchemaUtils.create(DBOrderTable)
    }

    override fun makeOrder(orderDrafts: List<OrderDraft>): List<Order> = transaction {
        val orderListId = UUID.randomUUID().toString()
        val orders = mutableListOf<Order>()
        var ordId: String
        for (order in orderDrafts) {
            ordId = DBOrderTable.insert {
                it[orderId] = orderListId
                it[id] = UUID.randomUUID().toString()
                it[username] = order.username
                it[menuItemId] = order.menuItemId
                it[businessId] = order.businessId
                it[phone] = order.phone
                it[address] = order.address
                it[date] = order.date
                it[time] = order.time
                it[payOnDelivery] = order.payOnDelivery
                it[quantity] = order.quantity
            } get DBOrderTable.id
            orders.add(getOrder(ordId)!!)
        }
        orders
    }

    override fun getUserOrders(username: String): List<Order> = transaction(db) {
        DBOrderTable.select { DBOrderTable.username eq username }.map {
            Order(
                it[DBOrderTable.orderId],
                it[DBOrderTable.id],
                it[DBOrderTable.username],
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

    override fun getVendorOrders(vendorId: String): List<Order> = transaction {
        DBOrderTable.select { DBOrderTable.businessId eq vendorId }.map {
            Order(
                it[DBOrderTable.orderId],
                it[DBOrderTable.id],
                it[DBOrderTable.username],
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

    override fun getOrder(id: String): Order? = transaction {
        DBOrderTable.select { DBOrderTable.id eq id }.map {
            Order(
                it[DBOrderTable.orderId],
                it[DBOrderTable.id],
                it[DBOrderTable.username],
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