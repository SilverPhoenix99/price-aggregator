package com.ekorn.port.websocket.bitstamp.model

data class SubscribeRequest(val data: SubscribeDataRequest) {
    val event: String = "bts:subscribe"
}

data class SubscribeDataRequest(val channel: String)
