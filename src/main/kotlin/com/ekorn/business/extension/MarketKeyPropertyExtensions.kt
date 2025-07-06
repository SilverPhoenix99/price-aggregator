package com.ekorn.business.extension

import com.ekorn.configuration.MarketKeyProperty

val MarketKeyProperty.marketSymbol: String
    get() = "${baseCurrency}${quoteCurrency}".lowercase()
