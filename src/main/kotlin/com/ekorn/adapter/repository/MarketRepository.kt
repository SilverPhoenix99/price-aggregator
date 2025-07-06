package com.ekorn.adapter.repository

import com.ekorn.business.model.MarketEntity
import com.ekorn.business.model.MarketKey
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.NativeQuery

interface MarketRepository : JpaRepository<MarketEntity, MarketKey> {

    // language=PostgreSQL
    @NativeQuery("SELECT * FROM markets WHERE symbol = :symbol")
    fun findBySymbol(symbol: String): MarketEntity?
}
