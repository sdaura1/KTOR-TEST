package com.foodvendor.entities

data class MenuItem(
    val id: String,
    val businessId: String,
    var name: String,
    var description: String,
    var price: Double
)