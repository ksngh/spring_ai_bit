package com.bitcoin.dto.value

data class CandleFeature(
    val time: String,        // 캔들 시간
    val open: Double,        // 시가
    val high: Double,        // 고가
    val low: Double,         // 저가
    val close: Double,       // 종가 (trade_price)
    val volume: Double,      // 거래량
    val value: Double        // 거래대금
)