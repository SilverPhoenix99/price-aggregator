package com.ekorn.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.HttpClient
import io.ktor.client.engine.java.Java
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.jackson.JacksonWebsocketContentConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KtorConfiguration {

    @Bean
    fun client(objectMapper: ObjectMapper): HttpClient {
        return HttpClient(Java) {
            install(WebSockets) {
                contentConverter = JacksonWebsocketContentConverter(objectMapper)
            }
        }
    }
}
