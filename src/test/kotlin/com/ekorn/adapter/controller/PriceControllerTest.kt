package com.ekorn.adapter.controller

import com.ekorn.adapter.controller.exception.NotFoundException
import com.ekorn.adapter.controller.mapper.toResponse
import com.ekorn.adapter.controller.model.PriceResponse
import com.ekorn.business.PriceService
import com.ekorn.business.model.Price
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class PriceControllerTest {

    companion object {
        const val BASE_CURRENCY = "BASE"
        const val QUOTE_CURRENCY = "QUOTE"
        const val MARKET_SYMBOL = "basequote"
    }

    @MockK
    lateinit var priceService: PriceService

    @InjectMockKs
    lateinit var subject: PriceController

    @Test
    fun `getPrice should return the price when found`() {

        // Given
        val price: Price = mockk()
        val priceResponse: PriceResponse = mockk()

        mockkStatic("com.ekorn.adapter.controller.mapper.PriceMappersKt")

        every { priceService.findPrice(MARKET_SYMBOL) } returns price
        every { price.toResponse() } returns priceResponse

        // When
        val actual = subject.getPrice(BASE_CURRENCY, QUOTE_CURRENCY)

        // Then
        assertThat(actual).isSameAs(priceResponse)
    }

    @Test
    fun `getPrice should throw when price wasn't found`() {

        // Given

        mockkStatic("com.ekorn.adapter.controller.mapper.PriceMappersKt")

        every { priceService.findPrice(MARKET_SYMBOL) } returns null

        // When
        val assertion = assertThatThrownBy { subject.getPrice(BASE_CURRENCY, QUOTE_CURRENCY) }

        // Then
        assertion.isExactlyInstanceOf(NotFoundException::class.java)
            .hasMessage("Price not found for BASE-QUOTE")

        verify { priceService.findPrice(MARKET_SYMBOL) }
        verify(exactly = 0) { any<Price>().toResponse() }
    }
}
