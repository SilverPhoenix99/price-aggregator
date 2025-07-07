package com.ekorn.business.extension

import com.ekorn.adapter.downstream.model.MarketResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MarketResponseExtensionsTest {
    @Test
    fun `marketSymbol should return the market symbol in lowercase`() {

        // Given
        val subject = MarketResponse(
            baseCurrency = "BASE",
            quoteCurrency = "QUOTE"
        )

        // When
        val actual = subject.marketSymbol

        // Then
        assertThat(actual).isEqualTo("basequote")
    }
}
