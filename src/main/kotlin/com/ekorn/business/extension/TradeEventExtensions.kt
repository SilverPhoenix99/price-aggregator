package com.ekorn.business.extension

import com.ekorn.adapter.websocket.bitstamp.model.TradeEvent

val TradeEvent.marketSymbol
    get() = channel.replaceFirst("live_trades_", "")
