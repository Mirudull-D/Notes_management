package com.REST.API.Entities

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("notes")
data class Notes(
    val title:String,
    val content: String,
    val colour:Long,
    val createdAt: Instant,
    val ownerId: ObjectId,
    @Id val id: ObjectId=ObjectId.get()
)
