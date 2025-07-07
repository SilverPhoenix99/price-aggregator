package com.ekorn.business

import com.ekorn.adapter.downstream.BitstampMarketsClient
import com.ekorn.adapter.downstream.model.MarketResponse
import com.ekorn.adapter.repository.MarketRepository
import com.ekorn.business.model.Market
import com.ekorn.business.model.MarketKey
import com.ekorn.configuration.AppProperties
import com.ekorn.configuration.MarketKeyProperty
import io.mockk.Called
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MarketServiceTest {

    @MockK
    lateinit var repository: MarketRepository;

    @MockK
    lateinit var marketsClient: BitstampMarketsClient;

    @MockK
    lateinit var app: AppProperties;

    @InjectMockKs
    lateinit var subject: MarketService

    @Test
    fun `initialiseMarkets saves matching markets`() {

        // Given
        val marketSlot = slot<List<Market>>()

        every { app.markets } returns setOf(
            MarketKeyProperty("BASE1", "QUOTE1"),
            MarketKeyProperty("BASE2", "QUOTE2"),
        )

        every { repository.findAll() } returns listOf(
            Market(key = MarketKey("BASE1", "QUOTE1"))
        )

        every { marketsClient.getMarkets() } returns listOf(
            MarketResponse("BASE1", "QUOTE1"),
            MarketResponse("BASE2", "QUOTE2"),
            MarketResponse("BASE3", "QUOTE3"),
        )

        every { repository.saveAll<Market>(any()) } returns emptyList()

        // When
        subject.initialiseMarkets()

        // Then
        verify { repository.findAll() }
        verify { repository.saveAll(capture(marketSlot)) }

        val expected = Market(key = MarketKey("BASE2", "QUOTE2"))

        assertThat(marketSlot.captured)
            .singleElement()
            .usingRecursiveComparison()
            .isEqualTo(expected)
    }

    @Test
    fun `initialiseMarkets doesn't save anything when configuration is empty`() {

        // Given
        every { app.markets } returns emptySet()

        // When
        subject.initialiseMarkets()

        // Then
        verify { listOf(repository, marketsClient) wasNot Called }
    }

    @Test
    fun `initialiseMarkets doesn't save anything when no markets match`() {

        // Given
        every { app.markets } returns setOf(
            MarketKeyProperty("BASE1", "QUOTE1"),
            MarketKeyProperty("BASE2", "QUOTE2"),
        )

        every { repository.findAll() } returns listOf(
            Market(key = MarketKey("BASE1", "QUOTE1"))
        )

        every { marketsClient.getMarkets() } returns listOf(
            MarketResponse("BASE3", "QUOTE3"),
        )

        // When
        subject.initialiseMarkets()

        // Then
        verify { repository.findAll() }
        verify(exactly = 0) { repository.saveAll<Market>(any()) }
    }

    @Test
    fun existsBySymbol() {

        // Given
        every { repository.existsBySymbol("test_symbol") } returns true

        // When
        val actual = subject.existsBySymbol("test_symbol")

        // Then
        assertThat(actual).isTrue()
    }
}
