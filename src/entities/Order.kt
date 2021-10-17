package com.foodvendor.entities

data class Order (
    val orderId: String,
    val id: String,
    var username: String,
    var menuItemId: String,
    var businessId: String,
    var quantity: Int,
    var address: String,
    var phone: String,
    var date: String,
    var time: String,
    var payOnDelivery: Boolean
)