package com.REST.API.Controllers

import com.REST.API.Dtos.NotesReponse
import com.REST.API.Dtos.NotesRequest
import com.REST.API.Entities.Notes
import com.REST.API.Repositories.NoteRepository
import org.bson.types.ObjectId
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import kotlin.time.Instant

@RestController
@RequestMapping("/notes")
class NotesController(
    private val noteRepository: NoteRepository
) {

//    @PostMapping("/")
//    fun upsert(body: NotesRequest): NotesReponse{
//        noteRepository.save(Notes(
//            title = body.title,
//            content = body.content,
//            colour = body.colour,
//            createdAt = java.time.Instant.now(),
//            ownerId = ObjectId(body.ownerId),
//
//        ))
//    }

}