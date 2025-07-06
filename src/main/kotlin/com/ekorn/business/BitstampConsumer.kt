package com.ekorn.business

import com.ekorn.adapter.websocket.bitstamp.WebSocketClient
import com.ekorn.adapter.websocket.bitstamp.model.BitstampEvent
import com.ekorn.business.extension.marketSymbol
import com.ekorn.configuration.AppProperties
import com.ekorn.configuration.MarketKeyProperty
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

        logger.info { "Market subscriptions: ${app.markets}" }

        val marketSymbols = app.markets
            .map(MarketKeyProperty::marketSymbol)

        webSocketClient.start(marketSymbols, this::consume)
    }

    suspend fun consume(event: BitstampEvent) {
        logger.info { "Received event: $event" }
    }
}
