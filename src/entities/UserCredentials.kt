package com.foodvendor.entities

data class UserCredentials(
    val token: String,
    val username: String,
    val phone: String,
    val address: String
)