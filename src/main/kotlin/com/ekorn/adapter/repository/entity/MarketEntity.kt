package com.ekorn.adapter.repository.entity

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

    @field:Column(nullable = false)
    open var baseDecimals: Int,

    @field:Column(nullable = false)
    open var quoteDecimals: Int,

    @field:Column
    open var price: BigDecimal?,

    @field:Column
    open var eventTimestamp: Instant?,

    @field:CreationTimestamp
    @field:Column(columnDefinition = "TIMESTAMP WITH TIME ZONE", updatable = false, nullable = false)
    open var createdAt: Instant?,

    @field:UpdateTimestamp
    @field:Column(columnDefinition = "TIMESTAMP WITH TIME ZONE", nullable = false)
    open var updatedAt: Instant?,
)

@Embeddable
open class MarketKey(

    @field:Column(columnDefinition = "VARCHAR", nullable = false)
    open var baseCurrency: String,

    @field:Column(columnDefinition = "VARCHAR", nullable = false)
    open var quoteCurrency: String,
)
