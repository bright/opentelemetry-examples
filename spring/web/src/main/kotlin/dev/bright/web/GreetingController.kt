package dev.bright.web

import io.opentelemetry.api.baggage.Baggage
import io.opentelemetry.api.trace.Span
import io.opentelemetry.context.Context
import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/greetings")
class GreetingController(private val service: GreetingService) {
    @PostMapping
    suspend fun create(@RequestBody greetingData: GreetingDto) {

        val senderKey = "sender"
        val baggage = Baggage.current()
            .toBuilder()
            .put(senderKey, greetingData.sender)
            .build()

        baggage.storeInContext(Context.current())
            .makeCurrent().use {
                Span.current()
                    .setAttribute(senderKey, baggage.getEntryValue(senderKey)!!)

                service.create(greetingData)
            }
    }

}
