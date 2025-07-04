package com.ekorn.port.websocket.model

import com.fasterxml.jackson.databind.JsonNode

data class UnknownEventResponse(
    override val event: String,
    val channel: String?,
    val data: JsonNode?
): EventResponse(event)
