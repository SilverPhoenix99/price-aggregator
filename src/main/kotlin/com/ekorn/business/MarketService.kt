package com.ekorn.business

import com.ekorn.adapter.downstream.BitstampMarketsClient
import com.ekorn.adapter.repository.MarketRepository
import com.ekorn.business.extension.marketSymbol
import com.ekorn.business.mapper.toDomain
import com.ekorn.business.model.MarketEntity
import com.ekorn.configuration.AppProperties
import com.ekorn.configuration.MarketKeyProperty
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.annotation.PostConstruct
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.Instant

@Service
class MarketService(
    private val marketRepository: MarketRepository,
    private val marketsClient: BitstampMarketsClient,
    private val app: AppProperties,
) {
    private val logger = KotlinLogging.logger {}

    @Retryable
    @PostConstruct
    @Transactional
    fun initialiseMarkets() {

        // It's expected to be a small number of entities.
        // Even 200 is considered a small amount to hold in memory.
        val entities = marketRepository.findAll()
            .associateBy(MarketEntity::symbol)

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

        marketRepository.saveAll(markets)
    }

    @Transactional
    fun updatePrice(
        symbol: String,
        price: BigDecimal,
        eventTimestamp: Instant
    ) {
        val market = marketRepository.findBySymbol(symbol)?.apply {
            this.price = price
            this.eventTimestamp = eventTimestamp
        }

        if (market == null) {
            // It's possible there's a new market that hasn't been picked up by the initialisation process.
            // In this case, the current implementation assumes that service will need to be restarted to pick it up.
            //
            // In a real-world scenario, we could call the GET/markets endpoint,
            // but we'd need to be careful to not call it too often,
            // by, e.g., caching some signal that the call was already attempted before.
            logger.info { "Skipping updating price for non-existing market: symbol=$symbol, price=$price" }
            return
        }

        logger.info { "Updating price for market: symbol=$symbol, price=$price" }

        marketRepository.save(market)
    }
}
