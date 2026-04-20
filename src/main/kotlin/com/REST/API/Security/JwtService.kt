package com.REST.API.Security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date

@Service
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

    fun generateAccessToken(userId: String):String{
        return generateToken(userId,"access",accessTokenValidity)
    }

    fun generateRefreshToken(userId: String):String{
        return generateToken(userId,"refresh",refreshTokenValidity)
    }

    fun validateAccessToken(token: String) : Boolean{
        val claims = parseAllClaims(token)?: return false
        val tokenType = claims["type"] ?: return false
        return tokenType == "access"
    }
    fun validateRefreshToken(token: String) : Boolean{
        val claims = parseAllClaims(token)?: return false
        val tokenType = claims["type"] ?: return false
        return tokenType == "refresh"
    }

    fun getUserIdFromToken(token: String): String? {
        val rawToken = if(token.startsWith("Bearer ")){
            token.removePrefix("Bearer ")
        }else token
        val claims = parseAllClaims(rawToken) ?:throw IllegalArgumentException("Invalid Token")

        return claims.subject
    }

    private fun parseAllClaims(token: String): Claims?{
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .payload
        }catch (e: Exception){
            null
        }
    }

}