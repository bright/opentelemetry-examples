package dev.bright.processor

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.repository.MongoRepository

data class Email(val message: String) {
    @Id
    var id: String? = null
}

interface EmailRepository : MongoRepository<Email, String>