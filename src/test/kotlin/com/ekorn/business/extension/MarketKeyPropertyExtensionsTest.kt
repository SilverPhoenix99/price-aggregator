package com.ekorn.business.extension

import com.ekorn.configuration.MarketKeyProperty
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MarketKeyPropertyExtensionsTest {

    @Test
    fun `marketSymbol should return the market symbol in lowercase`() {

        // Given
        val subject = MarketKeyProperty(
            baseCurrency = "BASE",
            quoteCurrency = "QUOTE"
        )

        // When
        val actual = subject.marketSymbol

        // Then
        assertThat(actual).isEqualTo("basequote")
    }

}
