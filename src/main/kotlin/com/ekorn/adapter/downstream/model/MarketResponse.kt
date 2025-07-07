package com.ekorn.adapter.downstream.model

import com.fasterxml.jackson.annotation.JsonProperty

data class MarketResponse(

    val baseCurrency: String,

    @field:JsonProperty("counter_currency")
    val quoteCurrency: String
)
