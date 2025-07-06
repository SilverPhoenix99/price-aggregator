package com.ekorn.business.mapper

import com.ekorn.adapter.downstream.model.MarketResponse
import com.ekorn.business.model.Market
import com.ekorn.business.model.MarketKey

fun MarketResponse.toDomain(): Market {
    return Market(
        key = MarketKey(
            baseCurrency = baseCurrency,
            quoteCurrency = quoteCurrency,
        )
    )
}
