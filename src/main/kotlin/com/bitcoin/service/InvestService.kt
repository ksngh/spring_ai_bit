package com.bitcoin.service

import com.bitcoin.client.UpbitClient
import com.bitcoin.entity.Investment
import com.bitcoin.enums.Opinion
import com.bitcoin.dto.request.prompt.UpbitPrompt
import com.bitcoin.dto.response.InvestDecisionResponse
import com.bitcoin.enums.MarketCode
import com.bitcoin.repository.InvestmentRepository
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.*
import kotlinx.coroutines.reactive.awaitSingle
import mu.KotlinLogging
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

    private val logger = KotlinLogging.logger {}

    @Scheduled(cron = "0 0,10,20,30,40,50 * * * *")
    suspend fun investAutomatically() = coroutineScope {
        val marketCode = MarketCode.CHRONOS

        // 1. 캔들 데이터 수집
        val upbit1minData = upbitClient.getCurrent1MinutesData(marketCode)
        val upbit5minData = upbitClient.getCurrent5MinutesData(marketCode)
        val upbit10minData = upbitClient.getCurrent10MinutesData(marketCode)

        // 2. 프롬프트 생성
        val prompt1min = Prompt(listOf(UserMessage(upbitPrompt.create1minInvestPrompt() + upbit1minData)))
        val prompt5min = Prompt(listOf(UserMessage(upbitPrompt.create5minInvestPrompt() + upbit5minData)))
        val prompt10min = Prompt(listOf(UserMessage(upbitPrompt.create10minInvestPrompt() + upbit10minData)))

        // 3. 병렬 실행
        val deferred1 = async { callChatAsync(prompt1min) }
        val deferred2 = async { callChatAsync(prompt5min) }
        val deferred3 = async { callChatAsync(prompt10min) }

        val responses = awaitAll(deferred1, deferred2, deferred3)

        // 4. 판단 로직
        var averageScore = 0.0
        var buyCount = 0
        for (response in responses) {
            logger.info("예측 결과: $response")
            if (response.decision == "BUY") {
                buyCount++
                averageScore += response.confidence
            }
        }

        // 5. 매수 조건: 3개 전부 BUY && 평균 0.7 이상
        if (buyCount == 3 && averageScore / 3 >= 0.7) {
            logger.info("✅ 매수 확정 신호: 평균 confidence = ${averageScore / 3}")
            investmentRepository.save(
                Investment(opinionTime = LocalDateTime.now(), opinion = Opinion.BUY)
            )
            upbitClient.buyAllKrwBalance()
        } else if (buyCount <= 1) {
            logger.info("🔻 매도 조건 충족: 매도 실행")
            upbitClient.sellAllBtc()
        } else {
            logger.info("보류")
        }
    }

    suspend fun callChatAsync(prompt: Prompt): InvestDecisionResponse = withContext(Dispatchers.IO) {

        val objectMapper = jacksonObjectMapper()
        val opinionJson = chatModel.call(prompt).result.output.text.toString()

        val jsonText = """\{.*}""".toRegex(RegexOption.DOT_MATCHES_ALL)
            .find(opinionJson)
            ?.value ?: throw IllegalArgumentException("유효한 JSON이 아닙니다.")

        val decision: InvestDecisionResponse = objectMapper.readValue(jsonText)

        return@withContext decision
    }

}