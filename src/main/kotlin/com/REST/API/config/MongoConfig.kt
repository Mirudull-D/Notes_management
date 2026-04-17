package com.REST.API.config

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MongoConfig {

    @Bean
    fun mongoClient(): MongoClient {
        return MongoClients.create(
            "mongodb+srv://dmirudull:cx6K7fwGuLBvOVQ9@kotlin-notes.ie79j4j.mongodb.net/Kotlin-Notes"
        )
    }
}