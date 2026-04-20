package com.REST.API.Security

import com.REST.API.Entities.RefreshToken
import com.REST.API.Entities.User
import com.REST.API.Repositories.RefreshTokenRepository
import com.REST.API.Repositories.UserRepository
import org.bson.types.ObjectId
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.MessageDigest
import java.time.Instant
import java.util.Base64

@Service
class AuthService(
    private val jwtService: JwtService,
    private val userRepository: UserRepository,
    private val hashEncoder: Hashing,
    private val refreshTokenRepository: RefreshTokenRepository
) {
    data class TokenPair(
        val accessToken: String,
        val refreshToken: String
    )

    fun register(email: String, password: String): User {
        return userRepository.save(
            User(
                email = email,
                password = hashEncoder.encode(password),
            )
        )
    }

    fun login(email: String, password: String): TokenPair {
        val user = userRepository.findByEmail(email)
            ?:throw BadCredentialsException("Invalid Credentials")

        if(!hashEncoder.matches(password, user.password)){
            throw BadCredentialsException("Invalid Credentials")
        }

        val newRefreshToken = jwtService.generateRefreshToken(user.id.toHexString())
        val newAccessToken = jwtService.generateAccessToken(user.id.toHexString())

        return TokenPair(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )
    }
        @Transactional
    fun refreshToken(token: String): TokenPair{
        if(!jwtService.validateRefreshToken(token)){
            throw IllegalArgumentException("Invalid Refresh Token")
        }
        val userId = jwtService.getUserIdFromToken(token)
        val user = userRepository.findById(ObjectId(userId))
            .orElseThrow{ IllegalArgumentException("User Not Found") }

        val hashed = hashToken(token)
        refreshTokenRepository.findByUserIdAndHashedToken(user.id, hashed)
            ?:throw IllegalArgumentException("Refresh Token Not Found")

        refreshTokenRepository.deleteByUserIdAndHashedToken(user.id, hashed)

        val newAccessToken = jwtService.generateAccessToken(userId!!)
        val newRefreshToken = jwtService.generateRefreshToken(userId)

        storeRefreshToken(user.id, newRefreshToken)

        return TokenPair(
            accessToken = newAccessToken,
            refreshToken = newRefreshToken
        )
    }

    private fun storeRefreshToken(userId: ObjectId, token: String) {
        val hashed = hashToken(token)
        val expiryMs = jwtService.refreshTokenValidity
        val expiresAt = Instant.now().plusMillis(expiryMs)

        refreshTokenRepository.save(RefreshToken(
            userId = userId,
            expiresAt = expiresAt,
            createdAt = Instant.now(),
            hashedToken = hashed
        ))

    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(token.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hash)
    }

}