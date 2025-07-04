package com.ekorn.adapter.repository

import com.ekorn.adapter.repository.entity.MarketEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MarketRepository : JpaRepository<MarketEntity, UUID> {
}
