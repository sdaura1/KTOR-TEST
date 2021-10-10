package com.foodvendor.entities

data class RegisterBody(
    val username: String,
    val password: String,
    val name: String,
    val phone: String,
    val address: String
)