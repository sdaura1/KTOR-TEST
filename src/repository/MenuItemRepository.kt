package com.foodvendor.repository

import com.foodvendor.entities.MenuItem
import com.foodvendor.entities.MenuItemDraft

interface MenuItemRepository {

    fun init()

    fun getMenuItem(id: String): MenuItem?

    fun getMenuItems(): List<MenuItem>

    fun getVendorMenuItems(vendorId: String): List<MenuItem>

    fun addMenuItem(menuItemDraft: MenuItemDraft): String

    fun deleteMenuItem(id: String): Int

    fun updateMenuItem(id: String, menuItemDraft: MenuItemDraft): Int
}