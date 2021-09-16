package com.foodvendor.repository

import com.foodvendor.entities.MenuItem
import com.foodvendor.entities.MenuItemDraft

class InMemoryMenuItemRepository: MenuItemRepository {

    private val menuItems = mutableListOf<MenuItem>(
        MenuItem("3462GH", "345678", "Tuwon Masara",
            "Mai dadi ne", 320.20),
        MenuItem("3462JI", "345678", "Tuwon Shinkafa",
            "Ba Nama", 220.10),
        MenuItem("3462MH", "456734", "Tuwon Alkama",
            "Na masu siga ne", 420.80),
        MenuItem("3462MI", "456734", "Tuwon Dawa",
            "Ga arha ga dadi", 120.80)
    )
    override fun getMenuItem(id: String): MenuItem? {
        return menuItems.firstOrNull{ it.id == id}
    }

    override fun getMenuItems(vendorId: String): List<MenuItem> {
        val menuItems = mutableListOf<MenuItem>()
        this.menuItems.forEach {
            if (it.businessId == vendorId){
                menuItems.add(it)
            }
        }
        return menuItems
    }

    override fun addMenuItem(menuItemDraft: MenuItemDraft): MenuItem {
        val index = menuItems.size + 2
        val menuItem = MenuItem(
            "12${index}42GH",
            menuItemDraft.businessId,
            menuItemDraft.name,
            menuItemDraft.description,
            menuItemDraft.price
        )
        menuItems.add(menuItem)
        return menuItem
    }

    override fun deleteMenuItem(id: String): Boolean {
        return menuItems.removeIf {
            it.id == id
        }
    }

    override fun updateMenuItem(id: String, menuItemDraft: MenuItemDraft): Boolean {
        val menuItem = menuItems.firstOrNull {it.id == id} ?: return false
        menuItem.description = menuItemDraft.description
        menuItem.price = menuItemDraft.price
        menuItem.name = menuItemDraft.name
        return true
    }
}