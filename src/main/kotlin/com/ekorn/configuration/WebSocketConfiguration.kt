package com.ekorn.configuration

import jakarta.annotation.PreDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class WebSocketConfiguration {

    @Bean
    fun webSocketScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob())
    }

    @PreDestroy
    fun shutdown() {
        webSocketScope().cancel("Application is shutting down")
    }
}
