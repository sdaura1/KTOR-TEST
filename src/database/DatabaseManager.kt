package com.foodvendor.database

import org.ktorm.database.Database

class DatabaseManager {

    private val hostName = "localhost"
    private val databaseName = "foodvendors"
    private val username = "postgres"
    private val password = "12345"

    private val ktorm: Database

    init {
        val jdbcUrl = "jdbc:pgsql://$hostName:8080/$databaseName?user=$username&password=$password&useSSL=false"
        ktorm = Database.connect(jdbcUrl)
    }
}