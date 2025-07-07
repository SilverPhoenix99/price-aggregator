package com.ekorn.business.mapper

import com.ekorn.adapter.downstream.model.MarketResponse
import com.ekorn.business.model.Market
import com.ekorn.business.model.MarketKey
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MarketResponseMappersTest {
    @Test
    fun `toDomain should return the entity`() {

        // Given
        val subject = MarketResponse(
            baseCurrency = "BASE",
            quoteCurrency = "QUOTE"
        )

        // When
        val actual = subject.toDomain()

        // Then
        val expected = Market(
            key = MarketKey(
                baseCurrency = "BASE",
                quoteCurrency = "QUOTE"
            )
        )

        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(expected)
    }
}
