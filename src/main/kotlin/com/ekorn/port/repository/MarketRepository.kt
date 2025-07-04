package com.ekorn.port.repository

import com.ekorn.port.repository.entity.MarketEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MarketRepository : JpaRepository<MarketEntity, UUID> {
}
