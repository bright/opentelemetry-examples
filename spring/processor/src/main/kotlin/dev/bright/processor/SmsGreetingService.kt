package dev.bright.processor

import io.opentelemetry.api.baggage.Baggage
import io.opentelemetry.api.trace.Span
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service

@Service
class SmsGreetingService(
    private val smsClient: SmsClient,
    private val smsRepository: SmsRepository
) {
    @Bean
    fun greetRecipientWithSms() = { greetedRecipientEvent: GreetedRecipientEvent ->

        val sender = Baggage.current().getEntryValue("sender")
        Span.current().setAttribute("sender", sender!!)

        smsClient.send(
            SmsClient.Sms(
                recipient = greetedRecipientEvent.recipient,
                message = greetedRecipientEvent.message
            )
        )
        smsRepository.save(Sms(greetedRecipientEvent.message))
    }
}