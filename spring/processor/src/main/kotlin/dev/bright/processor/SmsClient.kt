package dev.bright.processor

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SmsClient {
    private val log = LoggerFactory.getLogger(javaClass)
    fun send(sms: Sms) {
        log.info("Sending sms {}", sms)
    }

    data class Sms(val recipient: String, val message: String)
}