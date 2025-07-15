package com.bitcoin.dto.response

data class UpbitCurrentPriceResponse(
    val market: String,
    val tradePrice: Long,
    val timestamp: Long
)
