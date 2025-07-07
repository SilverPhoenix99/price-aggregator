package com.ekorn.business

import com.ekorn.adapter.websocket.bitstamp.WebSocketClient
import com.ekorn.adapter.websocket.bitstamp.model.SubscriptionSucceeded
import com.ekorn.adapter.websocket.bitstamp.model.TradeEvent
import com.ekorn.adapter.websocket.bitstamp.model.UnknownBitstampEvent
import com.ekorn.business.mapper.toPriceEntity
import com.ekorn.business.model.Price
import com.ekorn.configuration.AppProperties
import com.ekorn.configuration.MarketKeyProperty
import io.mockk.Called
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class BitstampConsumerTest {

    @MockK
    lateinit var app: AppProperties

    @MockK
    lateinit var webSocketClient: WebSocketClient

    @MockK
    lateinit var priceService: PriceService

    @InjectMockKs
    lateinit var subject: BitstampConsumer

    @Test
    fun `start should start the WebSocket client`() {

        // Given
        every { app.markets } returns setOf(
            MarketKeyProperty("BASE1", "QUOTE1"),
            MarketKeyProperty("BASE2", "QUOTE2"),
        )

        // Also counts as verification:
        every { webSocketClient.start(listOf("base1quote1", "base2quote2"), any()) } returns mockk<Job>()

        // When
        subject.start()

        // Then
        // Verified above
    }

    @Test
    fun `consume should update the price`() = runTest {

        // Given
        mockkStatic("com.ekorn.business.mapper.TradeEventMappersKt")

        val price: Price = mockk {
            every { marketSymbol } returns "test_symbol"
        }

        val event: TradeEvent = mockk {
            every { toPriceEntity() } returns price
        }

        // Also counts as verification:
        every { priceService.updatePrice(price) } just runs

        // When
        subject.consume(event)

        // Then
        // Verified above
    }

    @Test
    fun `consume should ignore subscription messages`() = runTest {

        // Given
        val event: SubscriptionSucceeded = mockk()

        // When
        subject.consume(event)

        // Then
        verify { priceService wasNot Called }
    }

    @Test
    fun `consume should ignore other messages`() = runTest {

        // Given
        val event: UnknownBitstampEvent = mockk()

        // When
        subject.consume(event)

        // Then
        verify { priceService wasNot Called }
    }
}
