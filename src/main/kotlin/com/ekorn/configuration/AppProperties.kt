package com.ekorn.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app")
data class AppProperties(
    val markets: Set<MarketKeyProperty>,
    val websocket: WebSocketProperties
)

data class WebSocketProperties(
    val url: String
)

data class MarketKeyProperty(
    val baseCurrency: String,
    val quoteCurrency: String
)
