package com.bitcoin.entity

import com.bitcoin.enums.Opinion
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("investment")
data class Investment(
    val id: Long? = null,
    val createdAt: LocalDateTime,
    val opinion: Opinion,
    val predictedAt: LocalDateTime,
    val predicted: Long,
    val reason: String,
    val candleIntervalMinutes: Int,     // 몇 분봉인지 (예: 1, 5, 10)
    val candleCount: Int
)