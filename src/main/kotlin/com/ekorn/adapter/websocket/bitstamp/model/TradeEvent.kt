package com.ekorn.adapter.websocket.bitstamp.model

import com.ekorn.adapter.websocket.bitstamp.MicrotimestampDeserializer
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.math.BigDecimal
import java.time.Instant

data class TradeEvent(
    override val event: String,
    val channel: String,
    val data: TradeEventData,
): BitstampEvent(event)

data class TradeEventData(
    @param:JsonProperty("price_str")
    val price: BigDecimal,
    @param:JsonProperty("microtimestamp")
    @param:JsonDeserialize(using = MicrotimestampDeserializer::class)
    val timestamp: Instant
)
