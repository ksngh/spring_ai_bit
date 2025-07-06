package com.bitcoin.entity

import com.bitcoin.enums.Opinion
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("investment")
data class Investment(
    val id: Long? = null,
    val opinionTime: LocalDateTime,
    val opinion: Opinion
)