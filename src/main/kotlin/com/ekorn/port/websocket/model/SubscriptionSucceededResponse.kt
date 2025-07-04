package com.ekorn.port.websocket.model

data class SubscriptionSucceededResponse(
    override val event: String,
    val channel: String,
): EventResponse(event)
