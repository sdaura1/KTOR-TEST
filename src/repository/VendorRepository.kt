package com.foodvendor.repository

import com.foodvendor.entities.VendorDraft
import com.foodvendor.entities.Vendor

interface VendorRepository {

    fun init()

    fun getAllVendors(): List<Vendor>

    fun getVendor(id: String): Vendor?

    fun addVendor(vendorDraft: VendorDraft): Vendor

    fun removeVendor(id: String): Int

    fun updateVendor(id: String, vendorDraft: VendorDraft): Int
}