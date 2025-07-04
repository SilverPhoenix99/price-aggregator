package com.ekorn.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationPropertiesScan
@EnableConfigurationProperties
@ComponentScan("com.ekorn.business", "com.ekorn.port")
class ApplicationConfiguration

@ConfigurationProperties("app")
data class AppProperties(
    val markets: List<MarketProperty>,
    val websocket: WebSocketProperties
) {
    data class WebSocketProperties(
        val url: String
    )

    data class MarketProperty(val base: String, val quote: String)
}
