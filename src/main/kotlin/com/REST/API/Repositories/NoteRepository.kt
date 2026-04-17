package com.REST.API.Repositories

import com.REST.API.Entities.Notes
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface NoteRepository: MongoRepository<Notes, ObjectId> {
    fun findByOwnerId(ownerId: ObjectId): List<Notes>
}