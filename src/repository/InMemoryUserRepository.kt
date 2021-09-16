package com.foodvendor.repository

class InMemoryUserRepository: UserRepository {

    private val credentialsToUsers = mapOf(
        "admin:admin" to UserRepository.User(1, "admin"),
        "shahid:12345" to UserRepository.User(2, "shahid"),
    )

    override fun getUser(username: String, password: String): UserRepository.User? {
        return credentialsToUsers["$username:$password"]
    }
}