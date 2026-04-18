package com.REST.API.Dtos

import org.bson.types.ObjectId

data class NotesRequest(
    val title:String,
    val content: String,
    val colour:Long,
    val id :String?
)
