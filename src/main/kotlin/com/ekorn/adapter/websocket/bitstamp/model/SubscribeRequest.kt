package com.ekorn.adapter.websocket.bitstamp.model

data class SubscribeRequest(val data: SubscribeDataRequest) {
    val event: String
        get() = "bts:subscribe"
}

data class SubscribeDataRequest(val channel: String)
