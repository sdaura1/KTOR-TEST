package com.foodvendor.repository

import com.foodvendor.entities.User
import com.foodvendor.entities.UserDraft

interface UserRepository {

    fun init()

    fun getUser(phone: String, password: String): User?

    fun getLoggedInUser(id: String): User?

    fun addUser(userDraft: UserDraft): User?

    fun deleteUser(id: String): Int

    fun updateUser(id: String, userDraft: UserDraft): Int
}