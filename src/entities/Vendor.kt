package com.foodvendor.entities

data class Vendor(
    val id: String,
    var name: String,
    var address: String,
    var phone: String,
    var businessName: String,
    var businessAddress: String,
    var businessPhone: String
)