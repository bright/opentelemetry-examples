package dev.bright.web

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

@ExperimentalCoroutinesApi
class GenerateLoad {
    val client = HttpClient {
        install(ContentNegotiation) {
            jackson()
        }
    }


    @Test
    fun run_som_load(): Unit = runBlocking {
        val counter = AtomicInteger()

        (1..10_000).chunked(200).forEach { chunk ->

            val maxConcurrent = Semaphore(Random.nextInt(5, 15))

            chunk.map { ix ->
                launch {
                    maxConcurrent.withPermit {
                        println("Stop ${counter.incrementAndGet()}")
                        delay(Random.nextLong(2000))
                        println("Running $ix")
                        client.post("http://localhost:8080/greetings") {
                            contentType(ContentType.Application.Json)
                            setBody(
                                GreetingDto(
                                    message = "With ❤️ from Ya!va Conf!",
                                    recipient = "Attendee #${Random.nextInt(1000)}",
                                    sender = "Sender #${Random.nextInt(1000)}"
                                )
                            )
                        }
                        println("Stop ${counter.getAndDecrement()}")
                    }
                }
            }.joinAll()
        }
    }

}
