package com.REST.API.Entities

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "refresh_tokens")
data class RefreshToken(
    val userId : ObjectId,
    @Indexed(expireAfter = "0s") //delete automatically
    val expiresAt : Instant,
    val createdAt : Instant = Instant.now(),
    val hashedToken : String
)
