package com.REST.API.Controllers

import com.REST.API.Dtos.NotesReponse
import com.REST.API.Dtos.NotesRequest
import com.REST.API.Entities.Notes
import com.REST.API.Repositories.NoteRepository
import org.bson.types.ObjectId
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import kotlin.time.Instant

@RestController
@RequestMapping("/notes")
class NotesController(
    private val noteRepository: NoteRepository
) {

    @PostMapping()
    fun upsert(@RequestBody body: NotesRequest): NotesReponse{
        val ownerId = (SecurityContextHolder.getContext().authentication?.principal  ) as String

        val noteId = if (body.id.isNullOrBlank() || !ObjectId.isValid(body.id)) {
            ObjectId()   // create new note
        } else {
            ObjectId(body.id)  // update existing
        }
        val note = noteRepository.save(Notes(
            id = noteId,
            title = body.title,
            content = body.content,
            colour = body.colour,
            createdAt = java.time.Instant.now(),
            ownerId = ObjectId(ownerId),

        ))

        return NotesReponse(
            id = note.id.toHexString(),
            title = note.title,
            content = note.content,
            colour = note.colour,
            createdAt = note.createdAt,
        )
    }

    @GetMapping
    fun getAllNotes(): List<NotesReponse> {
        val ownerId = (SecurityContextHolder.getContext().authentication?.principal  ) as String
        return noteRepository.findByOwnerId(ObjectId(ownerId) ).map { notes -> NotesReponse(
            id = notes.id.toHexString(),
            title = notes.title,
            content = notes.content,
            colour = notes.colour,
            createdAt = notes.createdAt,
        ) }
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String){
        val note = noteRepository.findById(ObjectId(id)).orElseThrow {
            IllegalArgumentException("Note not found")
        }
        val ownerId = (SecurityContextHolder.getContext().authentication?.principal) as String
        if(note.ownerId.toHexString() == ownerId){
            noteRepository.deleteById(ObjectId(id))
        }


    }
}