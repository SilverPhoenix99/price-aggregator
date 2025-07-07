package com.ekorn.business.mapper

import com.ekorn.adapter.websocket.bitstamp.model.TradeEvent
import com.ekorn.adapter.websocket.bitstamp.model.TradeEventData
import com.ekorn.business.model.Price
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Instant

class TradeEventMappersTest {
    @Test
    fun toPriceEntity() {

        // Given
        val now = Instant.now()

        val subject = TradeEvent(
            event = "test-trade-event",
            channel = "live_trades_test_symbol",
            data = TradeEventData(
                price = BigDecimal("123.45"),
                timestamp = now
            )
        )

        // When
        val actual = subject.toPriceEntity()

        // Then
        val expected = Price(
            marketSymbol = "test_symbol",
            price = BigDecimal("123.45"),
            eventTimestamp = now
        )

        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(expected)
    }

}
