package com.foodvendor.repository

import com.foodvendor.entities.Order
import com.foodvendor.entities.OrderDraft

interface OrderRepository {

    fun init()

    fun makeOrder(orderDrafts: List<OrderDraft>): List<Order>

    fun getUserOrders(username: String): List<Order>?

    fun getVendorOrders(vendorId: String): List<Order>?

    fun getOrder(id: String): Order?

    fun updateOrder(id: String, orderDraft: OrderDraft): Int

    fun removeOrder(id: String): Int
}