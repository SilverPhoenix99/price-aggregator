package com.ekorn.business.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MarketKeyTest {
    @Test
    fun `symbol should return the market symbol`() {

        // Given
        val subject = MarketKey(
            baseCurrency = "BASE",
            quoteCurrency = "QUOTE"
        )

        // When
        val actual = subject.symbol

        // Then
        assertThat(actual).isEqualTo("basequote")
    }
}
