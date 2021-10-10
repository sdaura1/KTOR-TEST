package com.foodvendor.authentication

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.auth.*
import io.ktor.auth.jwt.*

class JWTConfig(jwtSecret: String) {

    companion object Constants {
        private val jwtIssuer = "com.foodvendor"
        private val jwtRealm = "com.foodvendor.vendors"

        private const val CLAIM_USERID = "userId"
        private const val CLAIM_USERNAME = "userName"
    }

    private val jwtAlgorithm = Algorithm.HMAC512(jwtSecret)
    private val jwtVerifier: JWTVerifier = JWT
        .require(jwtAlgorithm)
        .withIssuer(jwtIssuer)
        .build()

    fun generateToken(user: JwtUser): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(jwtIssuer)
        .withClaim(CLAIM_USERID, user.userId)
        .withClaim(CLAIM_USERNAME, user.userName)
        .sign(jwtAlgorithm)

    fun configurationFeature(config: JWTAuthenticationProvider.Configuration)
            = with(config) {
        verifier(jwtVerifier)
        realm = jwtRealm
        validate {
            val userId = it.payload.getClaim(CLAIM_USERID).asString()
            val username = it.payload.getClaim(CLAIM_USERNAME).asString()

            if (userId != null && username != null){
                JwtUser(userId, username)
            }else{
                null
            }
        }
    }
    data class JwtUser(val userId: String, val userName: String): Principal
}