package com.ekorn.business.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.math.BigDecimal.ZERO
import java.time.Instant
import java.time.Instant.EPOCH

@Entity(name = "prices")
open class Price(
    @field:Id
    @field:Column(nullable = false, unique = true, updatable = false)
    open var marketSymbol: String,

    @field:Column(nullable = false)
    open var price: BigDecimal,

    @field:Column(nullable = false)
    open var eventTimestamp: Instant,

    @field:CreationTimestamp
    @field:Column(updatable = false, nullable = false)
    open var createdAt: Instant? = null,

    @field:UpdateTimestamp
    @field:Column(nullable = false)
    open var updatedAt: Instant? = null,
) {
    constructor() : this("", ZERO, EPOCH)
}
