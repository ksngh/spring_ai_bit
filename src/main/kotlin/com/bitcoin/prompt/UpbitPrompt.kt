package com.bitcoin.prompt

import org.springframework.ai.chat.prompt.Prompt
import org.springframework.stereotype.Component

@Component
class UpbitPrompt {

    private val contentBuilder = StringBuilder()

    fun createInvestPrompt(): String {
        contentBuilder.clear()
        contentBuilder.append(
            """
            너는 금융 데이터를 분석하여 단기 투자 판단을 내리는 전문가야.

            다음 조건을 반드시 지켜서 답변해:
            1. 너는 반드시 "BUY" 또는 "SELL" 중 하나의 단어만 출력해야 해. 그 외 어떤 설명도 하지 마.
            2. 내가 제공하는 1분 봉 데이터는 총 180개이며, 이는 약 3시간 치에 해당해.
            3. 너의 목적은: 마지막 캔들 기준으로 10분 후(10개 캔들 이후) 가격이 오를지 떨어질지를 예측하는 것이야.
            4. 상승할 것으로 예측되면 "BUY", 하락할 것으로 예측되면 "SELL"만 대답해.
            5. 중립, 판단 불가, 분석 결과 설명은 절대 하지 마.

            입력 데이터는 다음과 같은 구조야 (JSON 배열 형식):
            [
              {
                "candle_date_time_kst": "2025-07-06T19:00:00",
                "opening_price": 147663000,
                "high_price": 147663000,
                "low_price": 147566000,
                "trade_price": 147647000,
                "candle_acc_trade_price": 2075723.91072,
                "candle_acc_trade_volume": 0.01406475
              },
              ...
              (총 300개)
            ]

            이제 내가 제공하는 300개의 1분봉을 기반으로,
            10분 뒤 가격이 오를 것으로 예측되면 "BUY", 떨어질 것으로 보이면 "SELL" 중 하나만 대답해줘.
            """.trimIndent()
        )
        return contentBuilder.toString()
    }
}