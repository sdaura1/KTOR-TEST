package com.foodvendor.repository

import com.foodvendor.entities.VendorDraft
import com.foodvendor.entities.Vendor

class InMemoryVendorRepository: VendorRepository {

    private val vendors = mutableListOf(
        Vendor("345678", "shahid", "zoo road", "08066930554",
            "Ma'aji", "Startup Kano",
            "08104233583"),
        Vendor("456734", "Auna", "Minna", "08009930554",
            "Gidimo", "Lagos",
            "08104233588"),
    )

    override fun getAllVendor(): List<Vendor> {
        return vendors
    }

    override fun getVendor(id: String): Vendor? {
        return vendors.firstOrNull {
            it.id == id
        }
    }

    override fun addVendor(vendorDraft: VendorDraft): Vendor {
        val vendor = Vendor(
            "24569${vendors.size + 1}",
            vendorDraft.name,
            vendorDraft.address,
            vendorDraft.phone,
            vendorDraft.businessName,
            vendorDraft.businessAddress,
            vendorDraft.businessPhone,
        )

        vendors.add(vendor)
        return vendor
    }

    override fun removeVendor(id: String): Boolean {
        return vendors.removeIf {
            it.id == id
        }
    }

    override fun updateVendor(id: String, vendorDraft: VendorDraft): Boolean {
        val vendor = vendors.firstOrNull {it.id == id} ?: return false
        vendor.name = vendorDraft.name
        vendor.address = vendorDraft.address
        vendor.phone = vendorDraft.phone
        vendor.businessName = vendorDraft.businessName
        vendor.businessAddress = vendorDraft.businessAddress
        vendor.businessPhone = vendorDraft.businessPhone
        return true
    }

}