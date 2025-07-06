package com.ekorn.adapter.downstream

import com.ekorn.adapter.downstream.model.MarketResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "bitstamp-markets", url = "\${app.downstream.bitstamp-markets.url}")
interface BitstampMarketsClient {
    @GetMapping("/api/v2/markets/", consumes = [APPLICATION_JSON_VALUE])
    fun getMarkets(): List<MarketResponse>
}
