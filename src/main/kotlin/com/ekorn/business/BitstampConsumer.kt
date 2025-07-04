package com.ekorn.business

import com.ekorn.adapter.websocket.bitstamp.WebSocketClient
import com.ekorn.adapter.websocket.bitstamp.model.BitstampEvent
import com.ekorn.business.mapper.toMarketString
import com.ekorn.configuration.AppProperties
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component

/**
 * Handles the consumption of Bitstamp events.
 */
@Component
class BitstampConsumer(
    private val app: AppProperties,
    private val webSocketClient: WebSocketClient
) {
    private val logger = KotlinLogging.logger {}

    @PostConstruct
    fun start() {
        val markets = buildMarkets()
        webSocketClient.start(markets, this::consume)
    }

    fun buildMarkets(): List<String> {

        logger.info { "Market subscriptions: ${app.markets}" }
        return app.markets.map { prop -> prop.toMarketString() }
    }

    suspend fun consume(event: BitstampEvent) {
        logger.info { "Received event: $event" }
    }
}
