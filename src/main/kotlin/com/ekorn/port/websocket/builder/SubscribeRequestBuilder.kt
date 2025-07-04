package com.ekorn.port.websocket.builder

import com.ekorn.configuration.AppProperties.MarketProperty
import com.ekorn.port.websocket.model.SubscribeDataRequest
import com.ekorn.port.websocket.model.SubscribeRequest
import org.springframework.stereotype.Component

@Component
class SubscribeRequestBuilder {

    fun build(source: MarketProperty): SubscribeRequest {
        val channel = "live_trades_${source.base}${source.quote}".lowercase()
        return SubscribeRequest(SubscribeDataRequest(channel))
    }
}
