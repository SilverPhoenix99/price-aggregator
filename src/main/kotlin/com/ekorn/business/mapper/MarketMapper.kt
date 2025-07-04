package com.ekorn.business.mapper

import com.ekorn.configuration.AppProperties.MarketProperty

fun MarketProperty.toMarketString(): String {
    return "${this.base}${this.quote}".lowercase()
}
