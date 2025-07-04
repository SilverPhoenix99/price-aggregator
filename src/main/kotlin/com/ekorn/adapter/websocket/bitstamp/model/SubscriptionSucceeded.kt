package com.ekorn.adapter.websocket.bitstamp.model

data class SubscriptionSucceeded(
    override val event: String,
    val channel: String,
): BitstampEvent(event)
