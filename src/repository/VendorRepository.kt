package com.foodvendor.repository

import com.foodvendor.entities.VendorDraft
import com.foodvendor.entities.Vendor

interface VendorRepository {

    fun getAllVendor(): List<Vendor>

    fun getVendor(id: String): Vendor?

    fun addVendor(vendorDraft: VendorDraft): Vendor

    fun removeVendor(id: String): Boolean

    fun updateVendor(id: String, vendorDraft: VendorDraft): Boolean
}