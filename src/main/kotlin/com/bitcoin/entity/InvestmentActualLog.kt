package com.bitcoin.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("investment_actual_log")
data class InvestmentActualLog(
    @Id
    val id: Long? = null,

    val actualPrice: Long,

    val recordedAt: LocalDateTime = LocalDateTime.now().withSecond(0).withNano(0)

)