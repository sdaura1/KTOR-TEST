package com.foodvendor.repository

import com.foodvendor.entities.Order
import com.foodvendor.entities.OrderDraft

interface OrderRepository {

    fun makeOrder(orderDraft: OrderDraft): Order?

    fun getOrders(vendorId: String): List<Order>

    fun getOrder(id: String): Order?

    fun updateOrder(id: String, orderDraft: OrderDraft): Boolean

    fun removeOrder(id: String): Boolean
}