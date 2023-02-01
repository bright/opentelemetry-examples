package dev.bright.web

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.reactor.asFlux
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import kotlin.random.Random

@Component
class GreetingService(
    private val greetingsRepository: GreetingsRepository,
) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val greetedRecipientEvents = Channel<GreetedRecipientEvent>()

    suspend fun create(greetingDto: GreetingDto) {
        log.info("Creating greeting {}", greetingDto)
        val greeting = Greeting(greetingDto)

        withContext(Dispatchers.IO) {
            greetingsRepository.save(greeting)
            delay(Random.nextLong(2000))
        }

        greetedRecipientEvents.send(GreetedRecipientEvent(greeting))
        log.info("Done greeting {}", greetingDto)
    }

    @Bean
    fun greetedRecipientEvents(): () -> Flux<GreetedRecipientEvent> = {
        greetedRecipientEvents.consumeAsFlow().asFlux()
    }
}


data class GreetedRecipientEvent(val message: String, val recipient: String) {
    constructor(greeting: Greeting) : this(greeting.message, greeting.recipient)
}

fun Greeting(dto: GreetingDto) = Greeting(message = dto.message, recipient = dto.recipient)
