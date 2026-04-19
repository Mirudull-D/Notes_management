package com.REST.API.Security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import java.util.Date
import kotlin.io.encoding.Base64

class JwtService(@Value("\${jwt.secret}") private val jwtSecret : String) {

    private val secretKey = Keys.hmacShaKeyFor(java.util.Base64.getDecoder().decode(jwtSecret))

    private val accessTokenValidity = 15L*60L*1000L

    val refreshTokenValidity = 30L *24L*60*60*1000L

    private fun generateToken(
        userId: String,
        type:String,
        expiry: Long
    ): String{
        val now = Date()
        val expiryDate = Date(now.time + expiry)

        return Jwts.builder()
            .subject(userId)
            .claim("type",type)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secretKey,Jwts.SIG.HS256)
            .compact()
    }

    fun generateAccessToken(userId: String ):String{
        return generateToken(userId,"access",accessTokenValidity)
    }

    fun generateRefreshToken(userId: String):String{
        return generateToken(userId,"refresh",refreshTokenValidity)
    }

}