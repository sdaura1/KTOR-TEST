package com.foodvendor.repository

import com.foodvendor.database.DBMenuItemTable
import com.foodvendor.database.DBOrderTable
import com.foodvendor.entities.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class SQLMenuItemRepository(private val db: Database): MenuItemRepository {

    override fun init() = transaction(db){
        SchemaUtils.create(DBMenuItemTable)
    }

    override fun getMenuItem(id: String): MenuItem? = transaction{
        DBMenuItemTable.select { DBMenuItemTable.id eq id }.map {
            MenuItem(
                it[DBMenuItemTable.id],
                it[DBMenuItemTable.businessId],
                it[DBMenuItemTable.name],
                it[DBMenuItemTable.description],
                it[DBMenuItemTable.price]
            )
        }.singleOrNull()
    }

    override fun getMenuItems(): List<MenuItem> = transaction(db) {
        DBMenuItemTable.selectAll().map {
            MenuItem(
                it[DBMenuItemTable.id],
                it[DBMenuItemTable.businessId],
                it[DBMenuItemTable.name],
                it[DBMenuItemTable.description],
                it[DBMenuItemTable.price]
            )
        }
    }

    override fun getVendorMenuItems(vendorId: String): List<MenuItem> = transaction {
        DBMenuItemTable.select { DBMenuItemTable.businessId eq vendorId }.map {
            MenuItem(
                it[DBMenuItemTable.id],
                it[DBMenuItemTable.businessId],
                it[DBMenuItemTable.name],
                it[DBMenuItemTable.description],
                it[DBMenuItemTable.price]
            )
        }
    }

    override fun addMenuItem(menuItemDraft: MenuItemDraft): String = transaction {
        val uuid = UUID.randomUUID().toString()
        DBMenuItemTable.insert {
            it[id] = uuid
            it[businessId] = menuItemDraft.businessId
            it[name] = menuItemDraft.name
            it[description] = menuItemDraft.description
            it[price] = menuItemDraft.price
        }
        uuid
    }

    override fun deleteMenuItem(id: String): Int = transaction {
        val deleted = DBMenuItemTable.deleteWhere{ DBMenuItemTable.id eq id }
        deleted
    }

    override fun updateMenuItem(id: String, menuItemDraft: MenuItemDraft): Int = transaction{
        val updatedOrder = DBMenuItemTable.update({ DBMenuItemTable.id eq id }) {
            it[businessId] = menuItemDraft.businessId
            it[name] = menuItemDraft.name
            it[description] = menuItemDraft.description
            it[price] = menuItemDraft.price
        }
        updatedOrder
    }
}