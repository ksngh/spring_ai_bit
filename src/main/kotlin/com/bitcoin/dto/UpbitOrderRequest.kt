package com.bitcoin.dto

data class UpbitOrderRequest(
    val market: String,
    val side: String,      // "bid" (매수)
    val price: String,     // 보유 KRW 전부
    val ord_type: String   // "price" (시장가 매수)
)