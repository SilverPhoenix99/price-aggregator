package com.ekorn.business

import com.ekorn.adapter.repository.PriceRepository
import com.ekorn.business.model.Price
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PriceService(
    private val repository: PriceRepository,
    private val marketService: MarketService
) {
    companion object {
        private val logger = KotlinLogging.logger {}
    }

    @Transactional
    fun updatePrice(price: Price) {
        val marketExists = marketService.existsBySymbol(price.marketSymbol)
        if (!marketExists) {
            // It's possible there's a new market that hasn't been picked up by the initialisation process.
            // In this case, the current implementation assumes that service will need to be restarted to pick it up.
            //
            // In a real-world scenario, we could call the GET/markets endpoint,
            // but we'd need to be careful to not call it too often,
            // by, e.g., caching some signal that the call was already attempted before.
            logger.info {
                "Skipping updating price for non-existing market: symbol=${price.marketSymbol}, price=${price.price}, timestamp=${price.eventTimestamp}"
            }
            return
        }

        logger.info {
            "Updating price: symbol=${price.marketSymbol}, price=${price.price}, timestamp=${price.eventTimestamp}"
        }

        repository.save(price)
    }

    fun findPrice(marketSymbol: String): Price? {
        return repository.findById(marketSymbol).orElse(null)
    }
}
