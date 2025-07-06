package com.ekorn.adapter.controller.mapper

import com.ekorn.adapter.controller.model.PriceResponse
import com.ekorn.business.model.Price

fun Price.toResponse(): PriceResponse {
    return PriceResponse(
        lastPrice = price,
        timestamp = eventTimestamp
    )
}
