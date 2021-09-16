package com.foodvendor.repository

import com.foodvendor.entities.Order
import com.foodvendor.entities.OrderDraft

class InMemoryOrderRepository: OrderRepository {

    private val orders = mutableListOf(
        Order("34567", "3462GH", "345678", 3, "zoo road",
            "08066930554", "12-12-21", "12:20pm", false),
        Order("34261", "3462MJ", "345678", 1, "Kano",
            "08066930554", "12-12-21", "12:10pm", true),
        Order("34261", "3462MJ","456734", 5, "Minna",
            "08066930554", "12-12-21", "12:10pm", false),
        Order("34261", "3462GH", "456734", 2, "Minna",
            "08066930554", "12-12-21", "12:10pm", true)
    )

    override fun getOrders(vendorId: String): List<Order> {
        val orders = mutableListOf<Order>()
        this.orders.forEach {
            if (it.businessId == vendorId) {
                orders.add(it)
            }
        }
        return orders
    }

    override fun getOrder(id: String): Order? {
        return orders.firstOrNull{
            it.id == id
        }
    }

    override fun updateOrder(id: String, orderDraft: OrderDraft): Boolean {
        val order = orders.firstOrNull{it.id == id} ?: return false
        order.address = orderDraft.address
        order.date = orderDraft.date
        order.time = orderDraft.time
        order.quantity = orderDraft.quantity
        order.businessId = orderDraft.businessId
        order.phone = orderDraft.phone
        order.payOnDelivery = orderDraft.payOnDelivery
        return true
    }

    override fun removeOrder(id: String): Boolean {
        return orders.removeIf {
            it.id == id
        }
    }

    override fun makeOrder(orderDraft: OrderDraft): Order {
        val order = Order(
            "24569${orders.size + 2}",
            orderDraft.menuItemId,
            orderDraft.businessId,
            orderDraft.quantity,
            orderDraft.address,
            orderDraft.phone,
            orderDraft.date,
            orderDraft.time,
            orderDraft.payOnDelivery
        )
        orders.add(order)
        return order
    }
}