package dev.bright.processor

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service

@Service
class EmailGreetingService(
    private val emailClient: EmailClient,
    private val emailRepository: EmailRepository
) {
    @Bean
    fun greetRecipientWithEmail() = { greetedRecipientEvent: GreetedRecipientEvent ->
        emailClient.send(
            EmailClient.Email(
                recipient = greetedRecipientEvent.recipient,
                message = greetedRecipientEvent.message
            )
        )
        emailRepository.save(Email(greetedRecipientEvent.message))
    }
}

