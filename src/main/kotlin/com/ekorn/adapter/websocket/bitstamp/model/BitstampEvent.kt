package com.ekorn.adapter.websocket.bitstamp.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "event",
    visible = true,
    defaultImpl = UnknownBitstampEvent::class,
)
@JsonSubTypes(
    Type(SubscriptionSucceeded::class, name = "bts:subscription_succeeded"),
    Type(TradeEvent::class, name = "trade"),
)
sealed class BitstampEvent(
    open val event: String
)
