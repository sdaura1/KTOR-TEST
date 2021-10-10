package com.foodvendor.repository

import com.foodvendor.database.DBMenuItem
import com.foodvendor.database.DBOrderTable
import com.foodvendor.database.DBVendorTable
import com.foodvendor.database.DatabaseManager
import com.foodvendor.entities.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class SQLMenuItemRepository(private val db: Database): MenuItemRepository {

    override fun init() = transaction(db){
        SchemaUtils.create(DBOrderTable)
    }

    override fun getMenuItem(id: String): MenuItem? = transaction{
        DBMenuItem.select { DBMenuItem.id eq id }.map {
            MenuItem(
                it[DBMenuItem.id],
                it[DBMenuItem.businessId],
                it[DBMenuItem.name],
                it[DBMenuItem.description],
                it[DBMenuItem.price]
            )
        }.singleOrNull()
    }

    override fun getMenuItems(vendorId: String): List<MenuItem> = transaction(db) {
        DBMenuItem.selectAll().map {
            MenuItem(
                it[DBMenuItem.id],
                it[DBMenuItem.businessId],
                it[DBMenuItem.name],
                it[DBMenuItem.description],
                it[DBMenuItem.price]
            )
        }
    }

    override fun addMenuItem(menuItemDraft: MenuItemDraft): MenuItem = transaction {
        val id = DBMenuItem.insert {
            it[id] = UUID.randomUUID().toString()
            it[businessId] = menuItemDraft.businessId
            it[name] = menuItemDraft.name
            it[description] = menuItemDraft.description
            it[price] = menuItemDraft.price
        } get DBOrderTable.id
        getMenuItem(id)!!
    }

    override fun deleteMenuItem(id: String): Int = transaction {
        val deleted = DBMenuItem.deleteWhere{ DBMenuItem.id eq id }
        deleted
    }

    override fun updateMenuItem(id: String, menuItemDraft: MenuItemDraft): Int = transaction{
        val updatedOrder = DBMenuItem.update({ DBMenuItem.id eq id }) {
            it[businessId] = menuItemDraft.businessId
            it[name] = menuItemDraft.name
            it[description] = menuItemDraft.description
            it[price] = menuItemDraft.price
        }
        updatedOrder
    }
}