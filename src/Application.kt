package com.foodvendor

import com.foodvendor.authentication.JWTConfig
import com.foodvendor.entities.*
import com.foodvendor.repository.InMemoryMenuItemRepository
import com.foodvendor.repository.InMemoryOrderRepository
import com.foodvendor.repository.InMemoryUserRepository
import com.foodvendor.repository.InMemoryVendorRepository
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
    }.start(wait = true)
}

val jwtConfig = JWTConfig("SHAHIDsaniAbDuLlAhI")

fun Application.configureRouting(){

    install(CallLogging)
    install(ContentNegotiation){
        gson {
            setPrettyPrinting()
        }
    }
    install(Authentication){
        jwt {
            jwtConfig.configurationFeature(this)
        }
    }

    routing {

        val repo = InMemoryVendorRepository()
        val userRepo = InMemoryUserRepository()
        val orderRepo = InMemoryOrderRepository()
        val menuItemRepo = InMemoryMenuItemRepository()

        post("/login") {
            val loginBody = call.receive<LoginBody>()

            val user = userRepo.getUser(loginBody.username, loginBody.password)
            if (user == null){
                call.respond(HttpStatusCode.Unauthorized,
                    "Invalid Credentials")
                return@post
            }
            val token = jwtConfig
                .generateToken(JWTConfig.JwtUser(user.userId, user.username))
            call.respond(token)
        }

        authenticate {
            get("/me"){
                val user = call.authentication.principal as JWTConfig.JwtUser
                call.respond(user)
            }

            get("/vendor/{id}") {
                val id = call.parameters["id"]
                val vendor = repo.getVendor(id.toString())

                if (vendor == null) {
                    call.respond(HttpStatusCode.NotFound,
                        "Vendor not found")
                } else {
                    call.respond(vendor)
                }
            }

            put("/vendor/{id}") {
                val vendorDraft = call.receive<VendorDraft>()
                val vendorId = call.parameters["id"]
                if (vendorId == null) {
                    call.respond(HttpStatusCode.BadRequest,
                        "Insert an ID")
                    return@put
                }

                val updated = repo.updateVendor(vendorId, vendorDraft)

                if (updated){
                    call.respond(HttpStatusCode.OK)
                }else {
                    call.respond(HttpStatusCode.NotFound,
                        "No vendor with id $vendorId")
                }
            }

            delete("/vendor/{id}") {
                val vendorId = call.parameters["id"]

                if (vendorId == null){
                    call.respond(HttpStatusCode.BadRequest,
                        "Insert an ID")
                    return@delete
                }

                val removed = repo.removeVendor(vendorId)
                if (removed){
                    call.respond(HttpStatusCode.OK)
                }else {
                    call.respond(HttpStatusCode.NotFound,
                        "No vendor with id $vendorId")
                }
            }

            post("/vendor") {
                val vendorDraft = call.receive<VendorDraft>()
                val vendor = repo.addVendor(vendorDraft)
                call.respond(vendor)
            }

            get("/vendors") {
                call.respond(repo.getAllVendor())
            }

            post("/order") {
                val orderDraft = call.receive<OrderDraft>()
                val order = orderRepo.makeOrder(orderDraft)
                call.respond(order)
            }

            get("/orders"){
                val vendorId = call.receive<Vendor>()
                call.respond(orderRepo.getOrders(vendorId.id))
            }

            get("/order/{id}") {
                val id = call.parameters["id"]
                val order = orderRepo.getOrder(id.toString())

                if (order == null) {
                    call.respond(HttpStatusCode.NotFound,
                        "Order not found")
                } else {
                    call.respond(order)
                }
            }

            put("/order/{id}"){
                val orderDraft = call.receive<OrderDraft>()
                val orderId = call.parameters["id"]
                if (orderId == null) {
                    call.respond(HttpStatusCode.BadRequest,
                        "Insert an ID")
                    return@put
                }

                val updated = orderRepo.updateOrder(orderId, orderDraft)

                if (updated){
                    call.respond(HttpStatusCode.OK)
                }else {
                    call.respond(HttpStatusCode.NotFound,
                        "No vendor with id $orderId")
                }
            }

            delete("/order/{id}") {
                val orderId = call.parameters["id"]

                if (orderId == null){
                    call.respond(HttpStatusCode.BadRequest,
                        "Insert an ID")
                    return@delete
                }

                val removed = orderRepo.removeOrder(orderId)
                if (removed){
                    call.respond(HttpStatusCode.OK)
                }else {
                    call.respond(HttpStatusCode.NotFound,
                        "No vendor with id $orderId")
                }
            }

            post("/menu") {
                val menuItemDraft = call.receive<MenuItemDraft>()
                val menuItem = menuItemRepo.addMenuItem(menuItemDraft)
                call.respond(menuItem)
            }

            get("/menus"){
                val vendorId = call.receive<Vendor>()
                call.respond(menuItemRepo.getMenuItems(vendorId.id))
            }

            get("/menu/{id}") {
                val id = call.parameters["id"]
                val menuItem = menuItemRepo.getMenuItem(id.toString())

                if (menuItem == null) {
                    call.respond(HttpStatusCode.NotFound,
                        "Order not found")
                } else {
                    call.respond(menuItem)
                }
            }

            put("/menu/{id}"){
                val menuItemDraft = call.receive<MenuItemDraft>()
                val menuId = call.parameters["id"]
                if (menuId == null) {
                    call.respond(HttpStatusCode.BadRequest,
                        "Insert an ID")
                    return@put
                }

                val updated = menuItemRepo.updateMenuItem(menuId, menuItemDraft)

                if (updated){
                    call.respond(HttpStatusCode.OK)
                }else {
                    call.respond(HttpStatusCode.NotFound,
                        "No vendor with id $menuId")
                }
            }

            delete("/menu/{id}") {
                val menuId = call.parameters["id"]

                if (menuId == null){
                    call.respond(HttpStatusCode.BadRequest,
                        "Insert an ID")
                    return@delete
                }

                val removed = menuItemRepo.deleteMenuItem(menuId)
                if (removed){
                    call.respond(HttpStatusCode.OK)
                }else {
                    call.respond(HttpStatusCode.NotFound,
                        "No vendor with id $menuId")
                }
            }
        }
    }
}

