package dev.bright.web

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/greetings")
class GreetingController(private val service: GreetingService) {
    @PostMapping
    suspend fun create(@RequestBody greetingData: GreetingDto) {
        service.create(greetingData)
    }

}
