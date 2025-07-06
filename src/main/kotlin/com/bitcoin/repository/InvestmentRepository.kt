package com.bitcoin.repository

import com.bitcoin.entity.Investment
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface InvestmentRepository: ReactiveCrudRepository<Investment, Long> {
}