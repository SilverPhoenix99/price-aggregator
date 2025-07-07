package com.ekorn.adapter.controller.mapper

import com.ekorn.adapter.controller.model.PriceResponse
import com.ekorn.business.model.Price
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.Instant

class PriceMappersTest {

    val price: BigDecimal = BigDecimal("123.45")
    val eventTimestamp: Instant = Instant.parse("2025-07-06T00:00:00Z")

    @Test
    fun `toResponse should return PriceResponse`() {

        // Given
        val priceEntity = Price(
            marketSymbol = "irrelevant",
            price = price,
            eventTimestamp = eventTimestamp
        )

        // When
        val actual = priceEntity.toResponse()

        // Then
        val expected = PriceResponse(
            lastPrice = price,
            timestamp = eventTimestamp
        )

        assertThat(actual).isEqualTo(expected)
    }
}
