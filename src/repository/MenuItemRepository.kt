package com.foodvendor.repository

import com.foodvendor.entities.MenuItem
import com.foodvendor.entities.MenuItemDraft

interface MenuItemRepository {

    fun init()

    fun getMenuItem(id: String): MenuItem?

    fun getMenuItems(vendorId: String): List<MenuItem>

    fun addMenuItem(menuItemDraft: MenuItemDraft): MenuItem

    fun deleteMenuItem(id: String): Int

    fun updateMenuItem(id: String, menuItemDraft: MenuItemDraft): Int
}