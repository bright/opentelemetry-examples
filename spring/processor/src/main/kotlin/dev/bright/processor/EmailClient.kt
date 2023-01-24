package dev.bright.processor

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EmailClient {
    private val log = LoggerFactory.getLogger(javaClass)
    fun send(sms: Email) {
        log.info("Sending email {}", sms)
    }

    data class Email(val recipient: String, val message: String)
}