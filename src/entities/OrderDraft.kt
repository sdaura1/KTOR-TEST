package com.foodvendor.entities

data class OrderDraft (
    var menuItemId: String,
    val businessId: String,
    val quantity: Int,
    val address: String,
    val phone: String,
    val date: String,
    val time: String,
    val payOnDelivery: Boolean
)