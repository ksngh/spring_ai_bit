package com.bitcoin.dto.response

data class InvestDecisionResponse(
    val decision: String,       // "BUY" 또는 "SELL"
    val confidence: Double      // 0.0 ~ 1.0 사이 확률
)