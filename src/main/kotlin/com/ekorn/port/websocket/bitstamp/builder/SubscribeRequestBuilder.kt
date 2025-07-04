package com.ekorn.port.websocket.bitstamp.builder

import com.ekorn.port.websocket.bitstamp.model.SubscribeDataRequest
import com.ekorn.port.websocket.bitstamp.model.SubscribeRequest
import org.springframework.stereotype.Component

@Component
class SubscribeRequestBuilder {

    fun build(channel: String): SubscribeRequest {
        return SubscribeRequest(SubscribeDataRequest(channel))
    }
}
