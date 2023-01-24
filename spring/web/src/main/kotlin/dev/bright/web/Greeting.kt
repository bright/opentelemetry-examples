package dev.bright.web

import org.springframework.data.annotation.Id

class Greeting(
    val message: String, val recipient: String, @Id var id: Int? = null
)