package com.foodvendor.repository

interface UserRepository {

    fun init()

    fun getUser(phone: String, password: String): User?

    fun addUser(user: User): User?

    fun deleteUser(id: String): Int

    fun updateUser(id: String, userDraft: UserDraft): Int

    data class User(
        val id: String,
        val username: String,
        val password: String,
        val name: String,
        val phone: String,
        val address: String
    )

    data class UserDraft(
        val username: String,
        val password: String,
        val name: String,
        val phone: String,
        val address: String
    )
}