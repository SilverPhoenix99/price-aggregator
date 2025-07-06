package com.ekorn.business.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.Instant

@Entity(name = "markets")
open class MarketEntity(
    @field:EmbeddedId
    open var key: MarketKey,

    @field:Column
    open var price: BigDecimal? = null,

    @field:Column
    open var eventTimestamp: Instant? = null,

    @field:CreationTimestamp
    @field:Column(updatable = false, nullable = false)
    open var createdAt: Instant? = null,

    @field:UpdateTimestamp
    @field:Column(nullable = false)
    open var updatedAt: Instant? = null,
) {
    constructor() : this(MarketKey())

    val symbol: String
        get() = key.symbol
}

@Embeddable
open class MarketKey(

    @field:Column(nullable = false)
    open var baseCurrency: String,

    @field:Column(nullable = false)
    open var quoteCurrency: String,
) {
    constructor() : this(
        baseCurrency = "",
        quoteCurrency = ""
    )

    val symbol: String
        get() = "${baseCurrency}${quoteCurrency}".lowercase()
}
