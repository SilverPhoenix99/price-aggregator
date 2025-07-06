package com.ekorn.adapter.controller

import com.ekorn.adapter.controller.exception.NotFoundException
import com.ekorn.adapter.controller.mapper.toResponse
import com.ekorn.adapter.controller.model.PriceResponse
import com.ekorn.business.PriceService
import com.ekorn.business.model.MarketKey
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/prices")
class PriceController(
    private val priceService: PriceService
) {
    @GetMapping(
        path = ["/{baseCurrency}-{quoteCurrency}"],
        produces = [APPLICATION_JSON_VALUE]
    )
    fun getPrice(
        @PathVariable baseCurrency: String,
        @PathVariable quoteCurrency: String
    ): PriceResponse
    {
        val key = MarketKey(baseCurrency, quoteCurrency).symbol

        val price = priceService.findPrice(key)?.toResponse()

        if (price == null) {
            throw NotFoundException("Price not found for $baseCurrency-$quoteCurrency")
        }

        return price
    }
}
