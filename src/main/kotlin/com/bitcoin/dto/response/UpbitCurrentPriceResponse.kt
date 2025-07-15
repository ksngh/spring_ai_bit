package com.bitcoin.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class UpbitCurrentPriceResponse(
    val market: String,
    @JsonProperty("trade_price") val tradePrice: Long,
    val timestamp: Long
)
