package com.bitcoin.dto.response

data class InvestDecisionResponse(
    val decision: String,               // "BUY" 또는 "SELL"
    val predicted: Long,                // 예측된 10분 뒤 가격
    val confidence: Double,             // 0.0 ~ 1.0
    val reason: String,                 // 판단 근거 (100자 내외)
    val candleIntervalMinutes: Int,     // 분봉 단위 (1, 5, 10 등)
    val candleCount: Int                // 사용된 캔들 수
)