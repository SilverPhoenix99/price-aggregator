package com.ekorn.adapter.controller.model

import java.math.BigDecimal
import java.time.Instant

data class PriceResponse(
    val lastPrice: BigDecimal,
    val timestamp: Instant
)
