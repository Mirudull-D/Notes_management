package com.REST.API.Dtos

import java.time.Instant

data class NotesReponse(
    val id:String,
    val title:String,
    val content: String,
    val colour:Long,
    val createdAt: Instant
)