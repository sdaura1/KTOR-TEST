package com.foodvendor.database

import org.ktorm.database.Database

class DatabaseManager {

    private val hostName = "mysql-master.shahid"
    private val databaseName = "ktor-vendor"
    private val username = "ktor_vendor"
    private val password = "ktor_vendor"

    private val ktorm: Database

    init {
        val jdbcUrl = "jdbc:mysql://$hostName:3306/$databaseName?user=$username&password=$password&useSSL=false"
        ktorm = Database.connect(jdbcUrl)
    }
}