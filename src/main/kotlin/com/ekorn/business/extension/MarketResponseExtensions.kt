package com.ekorn.business.extension

import com.ekorn.adapter.downstream.model.MarketResponse

val MarketResponse.marketSymbol: String
    get() = "${baseCurrency}${quoteCurrency}".lowercase()
