package com.ekorn.port.websocket.bitstamp.model

import com.fasterxml.jackson.databind.JsonNode

data class UnknownBitstampEvent(
    override val event: String,
    val channel: String?,
    val data: JsonNode?
): BitstampEvent(event)
