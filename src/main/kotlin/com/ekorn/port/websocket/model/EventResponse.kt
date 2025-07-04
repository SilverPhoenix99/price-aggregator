package com.ekorn.port.websocket.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "event",
    visible = true,
    defaultImpl = UnknownEventResponse::class,
)
@JsonSubTypes(
    Type(SubscriptionSucceededResponse::class, name = "bts:subscription_succeeded"),
    Type(TradeEventResponse::class, name = "trade"),
)
sealed class EventResponse(
    open val event: String
)
