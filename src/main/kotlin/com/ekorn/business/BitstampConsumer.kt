package com.ekorn.business

import com.ekorn.adapter.websocket.bitstamp.WebSocketClient
import com.ekorn.adapter.websocket.bitstamp.model.BitstampEvent
import com.ekorn.adapter.websocket.bitstamp.model.SubscriptionSucceeded
import com.ekorn.adapter.websocket.bitstamp.model.TradeEvent
import com.ekorn.business.extension.marketSymbol
import com.ekorn.business.mapper.toPriceEntity
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
    private val webSocketClient: WebSocketClient,
    private val priceService: PriceService,
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    @PostConstruct
    fun start() {

        logger.info { "Market subscriptions: ${app.markets}" }

        val marketSymbols = app.markets
            .map(MarketKeyProperty::marketSymbol)

        webSocketClient.start(marketSymbols, this::consume)
    }

    suspend fun consume(event: BitstampEvent) {
        when (event) {
            is TradeEvent -> {
                logger.info { "Received trade event: $event" }
                val price = event.toPriceEntity()
                priceService.updatePrice(price)
            }
            is SubscriptionSucceeded -> {
                logger.info { "Successfully subscribed to channel ${event.channel}" }
            }
            else -> {
                logger.info { "Unknown event type: $event" }
            }
        }
    }
}
