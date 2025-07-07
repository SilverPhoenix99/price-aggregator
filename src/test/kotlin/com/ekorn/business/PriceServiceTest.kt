package com.ekorn.business

import com.ekorn.adapter.repository.PriceRepository
import com.ekorn.business.model.Price
import io.mockk.Called
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.Optional

class PriceServiceTest {

    @MockK
    lateinit var repository: PriceRepository

    @MockK
    lateinit var marketService: MarketService

    @InjectMockKs
    lateinit var subject: PriceService

    @Test
    fun `updatePrice should save price when market exists`() {

        // Given
        val price: Price = mockk {
            every { marketSymbol } returns "existing_market"
        }

        every { marketService.existsBySymbol("existing_market") } returns true

        every { repository.save(price) } returns price

        // When
        subject.updatePrice(price)

        // Then
        verify { repository.save(price) }
    }

    @Test
    fun `updatePrice should ignore price when market doesn't exist`() {

        // Given
        val price: Price = mockk {
            every { marketSymbol } returns "missing_market"
        }

        every { marketService.existsBySymbol("missing_market") } returns false

        // When
        subject.updatePrice(price)

        // Then
        verify { repository wasNot Called }
    }

    @Test
    fun `findPrice should return price when found`() {

        // Given
        val price: Price = mockk()

        every { repository.findById("existing_market") } returns Optional.of(price)

        // When
        val actual = subject.findPrice("existing_market")

        // Then
        assertThat(actual).isSameAs(price)
    }

    @Test
    fun `findPrice should return null when price not found`() {

        // Given

        every { repository.findById("nonexistent_market") } returns Optional.empty()

        // When
        val actual = subject.findPrice("nonexistent_market")

        // Then
        assertThat(actual).isNull()
    }
}
