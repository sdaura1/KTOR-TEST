package com.foodvendor

import com.foodvendor.authentication.JWTConfig
import com.foodvendor.entities.*
import com.foodvendor.repository.*
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
import io.netty.handler.codec.DateFormatter
import org.jetbrains.exposed.sql.Database
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

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

        val vendorRepo = SQLVendorRepository(
        Database.connect(url = "jdbc:pgsql://localhost:5432/foodvendors",
            user = "postgres", password = "12345")
        )
        vendorRepo.init()

        val userRepo = SQLUserRepository(
            Database.connect(url = "jdbc:pgsql://localhost:5432/foodvendors",
                user = "postgres", password = "12345")
        )
        userRepo.init()

        val orderRepo = SQLOrderRepository(
            Database.connect(url = "jdbc:pgsql://localhost:5432/foodvendors",
                user = "postgres", password = "12345"))
        orderRepo.init()

        val menuItemRepo = SQLMenuItemRepository(
            Database.connect(url = "jdbc:pgsql://localhost:5432/foodvendors",
                user = "postgres", password = "12345"))
        menuItemRepo.init()

        post("/login") {
            val loginBody = call.receive<LoginBody>()

            val user = userRepo.getUser(loginBody.username, loginBody.password)
            val token = jwtConfig.generateToken(
                JWTConfig.JwtUser(
                    user.username,
                    user.id
                )
            )
            val userCredentials = UserCredentials(
                token,
                user.username,
                user.phone,
                user.address
            )
            call.respond(userCredentials)
        }

        post("/register") {
            val registerBody = call.receive<RegisterBody>()

            val registerUser = UserDraft(
                registerBody.username,
                registerBody.password,
                registerBody.name,
                registerBody.phone,
                registerBody.address
            )

            val user = userRepo.addUser(registerUser)
            call.respond(user)
        }

        authenticate {

            get("/me"){
                val user = call.authentication.principal as JWTConfig.JwtUser
                val returnedUser = userRepo.getLoggedInUser(user.userName)
                call.respond(returnedUser)
            }

            get("/vendor/{id}") {
                val id = call.parameters["id"]
                val vendor = vendorRepo.getVendor(id.toString())

                if (vendor == null) {
                    call.respond(HttpStatusCode.NotFound, "Vendor not found")
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

                val updated = vendorRepo.updateVendor(vendorId, vendorDraft)
                val vendor = vendorRepo.getVendor(vendorId)

                if (updated > 0){
                    call.respond(vendor!!)
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

                val removed = vendorRepo.removeVendor(vendorId)
                if (removed > 0){
                    call.respond(HttpStatusCode.OK, "Deleted Successfully")
                }else {
                    call.respond(HttpStatusCode.NotFound,
                        "No vendor with id $vendorId")
                }
            }

            post("/vendor") {
                val vendorDraft = call.receive<VendorDraft>()
                val vendor = vendorRepo.addVendor(vendorDraft)
                call.respond(vendor)
            }

            get("/vendors") {
                call.respond(vendorRepo.getAllVendors())
            }

            post("/order") {
                val orderDrafts = call.receive<Array<OrderDraft>>()
                val user = call.authentication.principal as JWTConfig.JwtUser
                val preparedList = mutableListOf<OrderDraft>()
                for (ord in orderDrafts){
                    preparedList.add(
                        OrderDraft(
                            user.userName,
                            ord.menuItemId,
                            ord.businessId,
                            ord.quantity,
                            ord.address,
                            ord.phone,
                            ord.date,
                            ord.time,
                            ord.payOnDelivery
                        )
                    )
                }
                val orders = orderRepo.makeOrder(preparedList)
                call.respond(HttpStatusCode.Created, orders)
            }

            get("/orders") {
                val user = call.authentication.principal as JWTConfig.JwtUser
                val order = orderRepo.getUserOrders(user.userName)

                call.respond(order)
            }

            get("/vendor/orders") {
                val user = call.authentication.principal as JWTConfig.JwtUser
                val order = orderRepo.getUserOrders(user.userName)

                call.respond(order)
            }

            get("/order/{id}") {
                val id = call.parameters["id"]
                val order = orderRepo.getOrder(id.toString())

                if (order == null) {
                    call.respond(HttpStatusCode.NotFound, "Order not found")
                } else {
                    call.respond(order)
                }
            }

            put("/order/{id}"){
                val orderDraft = call.receive<OrderDraft>()
                val orderId = call.parameters["id"]
                if (orderId == null) {
                    call.respond(HttpStatusCode.BadRequest, "Insert an ID")
                    return@put
                }

                val updated = orderRepo.updateOrder(orderId, orderDraft)
                val order = orderRepo.getOrder(orderId)

                if (updated > 0){
                    call.respond(order!!)
                }else {
                    call.respond(HttpStatusCode.NotFound,
                        "No vendor with id $orderId")
                }
            }

            delete("/order/{id}") {
                val orderId = call.parameters["id"]

                if (orderId == null){
                    call.respond(HttpStatusCode.BadRequest, "Insert an ID")
                    return@delete
                }

                val removed = orderRepo.removeOrder(orderId)
                if (removed > 0){
                    call.respond(HttpStatusCode.OK, "Order Deleted Successfully")
                }else {
                    call.respond(HttpStatusCode.NotFound,
                        "No order with id $orderId")
                }
            }

            post("/menu") {
                val menuItemDraft = call.receive<MenuItemDraft>()
                val uuid = menuItemRepo.addMenuItem(menuItemDraft)
                val menuItem = MenuItem(
                    uuid,
                    menuItemDraft.businessId,
                    menuItemDraft.name,
                    menuItemDraft.description,
                    menuItemDraft.price
                )
                call.respond(menuItem)
            }

            get("/menus"){
                val user = call.authentication.principal as JWTConfig.JwtUser
                call.respond(menuItemRepo.getMenuItems())
            }

            get("/vendor/menus"){
                val user = call.authentication.principal as JWTConfig.JwtUser
                call.respond(menuItemRepo.getVendorMenuItems(user.userName))
            }

            get("/menu/{id}") {
                val id = call.parameters["id"]
                val menuItem = menuItemRepo.getMenuItem(id.toString())

                if (menuItem == null) {
                    call.respond(HttpStatusCode.NotFound, "Order not found")
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
                val menuItem = menuItemRepo.getMenuItem(menuId)

                if (updated > 0){
                    call.respond(menuItem!!)
                }else {
                    call.respond(HttpStatusCode.NotFound, "No vendor with id $menuId")
                }
            }

            delete("/menu/{id}") {
                val menuId = call.parameters["id"]

                if (menuId == null){
                    call.respond(HttpStatusCode.BadRequest, "Insert an ID")
                    return@delete
                }

                val removed = menuItemRepo.deleteMenuItem(menuId)
                if (removed > 0){
                    call.respond(HttpStatusCode.OK,
                        "Menu Item removed Successfully")
                }else {
                    call.respond(HttpStatusCode.NotFound,
                        "No vendor with id $menuId")
                }
            }
        }
    }
}

