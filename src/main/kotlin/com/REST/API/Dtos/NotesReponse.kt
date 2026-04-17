package com.REST.API.Dtos

import java.time.Instant

data class NotesReponse(
    val title:String,
    val content: String,
    val colour:Long,
    val createdAt: Instant
)