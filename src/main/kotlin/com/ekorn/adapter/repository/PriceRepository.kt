package com.ekorn.adapter.repository

import com.ekorn.business.model.Price
import org.springframework.data.jpa.repository.JpaRepository

interface PriceRepository: JpaRepository<Price, String>
