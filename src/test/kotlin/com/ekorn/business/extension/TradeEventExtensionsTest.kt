package com.ekorn.business.extension

import com.ekorn.adapter.websocket.bitstamp.model.TradeEvent
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TradeEventExtensionsTest {
    @Test
    fun `marketSymbol should parse and return the market symbol`() {

        // Given
        val subject = TradeEvent(
            event = "test-event",
            channel = "live_trades_test_symbol",
            data = mockk()
        )

        // When
        val actual = subject.marketSymbol

        // Then
        assertThat(actual).isEqualTo("test_symbol")
    }

}
