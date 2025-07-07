package com.ekorn.business.mapper

import com.ekorn.adapter.websocket.bitstamp.model.TradeEvent
import com.ekorn.business.extension.marketSymbol
import com.ekorn.business.model.Price

fun TradeEvent.toPriceEntity(): Price {
    return Price(
        marketSymbol = marketSymbol,
        price = data.price,
        eventTimestamp = data.timestamp
    )
}
