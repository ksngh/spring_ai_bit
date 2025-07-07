package com.bitcoin.controller

import com.bitcoin.client.UpbitClient
import com.bitcoin.dto.UpbitCandleResponse
import com.bitcoin.service.InvestService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.prompt.Prompt

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chat")
class ChatController(
    private val upbitClient: UpbitClient,
) {

    @GetMapping
    suspend fun chat() : String {
        return upbitClient.buyAllKrwBalance()
    }

    @GetMapping("/test")
    suspend fun cha1t() : String {
        return upbitClient.sellAllBtc()
    }

}

