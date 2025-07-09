package com.bitcoin.controller

import com.bitcoin.client.UpbitClient
import com.bitcoin.service.InvestService

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chat")
class ChatController(
    private val upbitClient: UpbitClient,
    private val investService: InvestService
) {

    @GetMapping
    suspend fun chat() : Unit {
        investService.investAutomatically()
    }

    @GetMapping("/test")
    suspend fun cha1t() : String {
        return upbitClient.sellAllBtc()
    }

}

