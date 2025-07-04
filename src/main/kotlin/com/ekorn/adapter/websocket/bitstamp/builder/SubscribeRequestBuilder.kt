package com.ekorn.adapter.websocket.bitstamp.builder

import com.ekorn.adapter.websocket.bitstamp.model.SubscribeDataRequest
import com.ekorn.adapter.websocket.bitstamp.model.SubscribeRequest
import org.springframework.stereotype.Component

@Component
class SubscribeRequestBuilder {

    fun build(channel: String): SubscribeRequest {
        return SubscribeRequest(SubscribeDataRequest(channel))
    }
}
