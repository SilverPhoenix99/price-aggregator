package com.ekorn.business.mapper

import com.ekorn.adapter.downstream.model.MarketResponse
import com.ekorn.business.model.MarketEntity
import com.ekorn.business.model.MarketKey

fun MarketResponse.toDomain(): MarketEntity {
    return MarketEntity(
        key = MarketKey(
            baseCurrency = baseCurrency,
            quoteCurrency = quoteCurrency,
        )
    )
}
