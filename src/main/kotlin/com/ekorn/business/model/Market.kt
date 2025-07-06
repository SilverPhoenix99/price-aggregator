package com.ekorn.business.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Transient
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant

@Entity(name = "markets")
open class Market(
    @field:EmbeddedId
    open var key: MarketKey,

    @field:CreationTimestamp
    @field:Column(updatable = false, nullable = false)
    open var createdAt: Instant? = null,

    @field:UpdateTimestamp
    @field:Column(nullable = false)
    open var updatedAt: Instant? = null,
) {
    constructor() : this(MarketKey())
}

@Embeddable
open class MarketKey(

    @field:Column(nullable = false, updatable = false)
    open var baseCurrency: String,

    @field:Column(nullable = false, updatable = false)
    open var quoteCurrency: String,
) {
    constructor() : this(
        baseCurrency = "",
        quoteCurrency = ""
    )

    val symbol: String
        @Transient
        get() = "${baseCurrency}${quoteCurrency}".lowercase()
}
