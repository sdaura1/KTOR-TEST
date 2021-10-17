package com.foodvendor.repository

import com.foodvendor.database.DBVendorTable
import com.foodvendor.database.DatabaseManager
import com.foodvendor.entities.Vendor
import com.foodvendor.entities.VendorDraft
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class SQLVendorRepository(private val db: Database): VendorRepository {

    override fun init() = transaction(db){
        SchemaUtils.create(DBVendorTable)
    }

    override fun getAllVendors(): List<Vendor> = transaction(db){
        DBVendorTable.selectAll().map {
            Vendor(
                it[DBVendorTable.id].toString(),
                it[DBVendorTable.name],
                it[DBVendorTable.address],
                it[DBVendorTable.phone],
                it[DBVendorTable.businessName],
                it[DBVendorTable.businessAddress],
                it[DBVendorTable.businessPhone]
            )
        }
    }

    override fun getVendor(id: String): Vendor? = transaction(db) {
        DBVendorTable.select { DBVendorTable.id eq id }.map {
            Vendor(
                it[DBVendorTable.id],
                it[DBVendorTable.name],
                it[DBVendorTable.address],
                it[DBVendorTable.phone],
                it[DBVendorTable.businessName],
                it[DBVendorTable.businessAddress],
                it[DBVendorTable.businessPhone]
            )
        }.singleOrNull()
    }

    override fun addVendor(vendorDraft: VendorDraft): Vendor = transaction(db) {
       val id = DBVendorTable.insert {
            it[id] = UUID.randomUUID().toString()
            it[name] = vendorDraft.name
            it[address] = vendorDraft.address
            it[phone] = vendorDraft.phone
            it[businessName] = vendorDraft.businessName
            it[businessAddress] = vendorDraft.businessAddress
            it[businessPhone] = vendorDraft.businessPhone
        } get DBVendorTable.id
        getVendor(id)!!
    }

    override fun removeVendor(id: String): Int = transaction {
        val deleted = DBVendorTable.deleteWhere{ DBVendorTable.id eq id }
        deleted
    }

    override fun updateVendor(id: String, vendorDraft: VendorDraft): Int  = transaction{
        val updatedUser = DBVendorTable.update({ DBVendorTable.id eq id }) {
            it[name] = vendorDraft.name
            it[address] = vendorDraft.address
            it[phone] = vendorDraft.phone
            it[businessName] = vendorDraft.businessName
            it[businessAddress] = vendorDraft.businessAddress
            it[businessPhone] = vendorDraft.businessPhone
        }
        updatedUser
    }
}