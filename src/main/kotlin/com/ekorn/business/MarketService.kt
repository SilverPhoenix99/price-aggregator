package com.ekorn.business

import com.ekorn.adapter.downstream.BitstampMarketsClient
import com.ekorn.adapter.repository.MarketRepository
import com.ekorn.business.extension.marketSymbol
import com.ekorn.business.mapper.toDomain
import com.ekorn.configuration.AppProperties
import com.ekorn.configuration.MarketKeyProperty
import jakarta.annotation.PostConstruct
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MarketService(
    private val repository: MarketRepository,
    private val marketsClient: BitstampMarketsClient,
    private val app: AppProperties,
) {
    @Retryable
    @PostConstruct
    @Transactional
    fun initialiseMarkets() {

        // It's expected to be a small number of entities.
        // Even 200 is considered a small amount to hold in memory.
        val entities = repository.findAll()
            .associateBy { market -> market.key.symbol }

        val configuredMarketSymbols = app.markets
            .map(MarketKeyProperty::marketSymbol)
            .toSet()

        val markets = marketsClient.getMarkets()
            .filter { marketResponse ->
                // Keep configured markets only
                configuredMarketSymbols.contains(marketResponse.marketSymbol)
            }
            .mapNotNull { market ->

                val entity = entities[market.marketSymbol]
                if (entity == null) {
                    // The entity doesn't exist yet, so just create it now
                    market.toDomain()
                }
                else {
                    // Entity exists.
                    null
                }
            }

        repository.saveAll(markets)
    }

    fun findBySymbol(symbol: String) = repository.findBySymbol(symbol)
}
