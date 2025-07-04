package com.ekorn.port.websocket.bitstamp.model

data class SubscriptionSucceeded(
    override val event: String,
    val channel: String,
): BitstampEvent(event)
