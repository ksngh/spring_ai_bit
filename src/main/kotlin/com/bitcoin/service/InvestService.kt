package com.bitcoin.service

import com.bitcoin.client.UpbitClient
import com.bitcoin.entity.Investment
import com.bitcoin.enums.Opinion
import com.bitcoin.prompt.UpbitPrompt
import com.bitcoin.repository.InvestmentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.withContext
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class InvestService(
    private val upbitClient: UpbitClient,
    private val investmentRepository: InvestmentRepository,
    private val chatModel: ChatModel,
    private val upbitPrompt: UpbitPrompt
) {

    @Scheduled(cron = "0 0,10,20,30,40,50 * * * *")
    suspend fun save5MinutesData(): String {
        val upbitData = upbitClient.getCurrent5MinutesData()
        val prompt = Prompt(listOf(UserMessage(upbitPrompt.createInvestPrompt() + upbitData)))
        val response = callChatAsync(prompt)
        return response
    }

    suspend fun callChatAsync(prompt: Prompt): String = withContext(Dispatchers.IO) {

        val opinionString = chatModel.call(prompt).result.output.text.toString()
        val opinionEnum = Opinion.valueOf(opinionString.uppercase())

        val investment = Investment(
            id = null,
            opinionTime = LocalDateTime.now(),
            opinion = opinionEnum
        )

        investmentRepository.save(investment).awaitSingle()

        if(opinionString=="BUY"){
            upbitClient.buyAllKrwBalance()
        }else{
            upbitClient.sellAllBtc()
        }
        return@withContext opinionString
    }

}