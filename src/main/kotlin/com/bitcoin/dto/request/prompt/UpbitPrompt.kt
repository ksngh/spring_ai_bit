package com.bitcoin.dto.request.prompt

import org.springframework.ai.chat.prompt.Prompt
import org.springframework.stereotype.Component

@Component
class UpbitPrompt {

    private val contentBuilder = StringBuilder()

    fun create1minInvestPrompt(): String {
        contentBuilder.clear()
        contentBuilder.append(
            """
            너는 금융 데이터를 분석하여 단기 투자 판단을 내리는 전문가야.
            
            내가 제공하는 데이터는 1분봉 기준의 시세 정보이며, 총 300개의 캔들이야.  
            
            너의 목적은 **마지막 캔들을 기준으로 10분 뒤 가격이 상승할지 하락할지를 예측**하는 것이다.
            
            ### 반드시 다음 조건을 지켜줘:
            
            1. 출력은 아래 JSON 형식으로만 해. **절대로 다른 설명이나 주석을 추가하지 마.**
            2. 판단은 `BUY` 또는 `SELL` 중 하나만 골라야 해.
            3. 응답 JSON은 반드시 아래 형식을 따라야 해:

            {
              "decision": "BUY 또는 SELL",
              "confidence": 0.0 ~ 1.0 사이의 소수 (예측 판단의 확신도),
            }
            confidence는 너의 예측이 얼마나 신뢰할 만한지를 수치로 표현해. (높을수록 확신)

            참고: 입력 데이터 구조는 다음과 같아
            [
              {
                "open": 42600000,
                "high": 42650000,
                "low": 42580000,
                "close": 42620000,
                "volume": 12.3,
                "value": 521000000.0
              },
              ...
            ]
            
            이제 위 300개의 캔들을 분석해서, 마지막 시점 기준으로 10분 후 가격이 오를지/내릴지 판단해줘.
            출력은 반드시 JSON만 해.
            """.trimIndent()
        )
        return contentBuilder.toString()
    }

    fun create5minInvestPrompt(): String {
        contentBuilder.clear()
        contentBuilder.append(
            """
            너는 금융 데이터를 분석하여 단기 투자 판단을 내리는 전문가야.
            
            내가 제공하는 데이터는 5분봉 기준의 시세 정보이며, 총 60개의 캔들이야.  
            
            너의 목적은 **마지막 캔들을 기준으로 10분 뒤 가격이 상승할지 하락할지를 예측**하는 것이다.
            
            ### 반드시 다음 조건을 지켜줘:
            
            1. 출력은 아래 JSON 형식으로만 해. **절대로 다른 설명이나 주석을 추가하지 마.**
            2. 판단은 `BUY` 또는 `SELL` 중 하나만 골라야 해.
            3. 응답 JSON은 반드시 아래 형식을 따라야 해:

            {
              "decision": "BUY 또는 SELL",
              "confidence": 0.0 ~ 1.0 사이의 소수 (예측 판단의 확신도),
            }
            confidence는 너의 예측이 얼마나 신뢰할 만한지를 수치로 표현해. (높을수록 확신)
            
            참고: 입력 데이터 구조는 다음과 같아
            [
              {
                "open": 42600000,
                "high": 42650000,
                "low": 42580000,
                "close": 42620000,
                "volume": 12.3,
                "value": 521000000.0
              },
              ...
            ]
            
            이제 위 60개의 캔들을 분석해서, 마지막 시점 기준으로 10분 후 가격이 오를지/내릴지 판단해줘.
            출력은 반드시 JSON만 해.
            """.trimIndent()
        )
        return contentBuilder.toString()
    }

    fun create10minInvestPrompt(): String {
        contentBuilder.clear()
        contentBuilder.append(
            """
            너는 금융 데이터를 분석하여 단기 투자 판단을 내리는 전문가야.
            
            내가 제공하는 데이터는 10분봉 기준의 시세 정보이며, 총 30개의 캔들이야.  
            
            너의 목적은 **마지막 캔들을 기준으로 10분 뒤 가격이 상승할지 하락할지를 예측**하는 것이다.
            
            ### 반드시 다음 조건을 지켜줘:
            
            1. 출력은 아래 JSON 형식으로만 해. **절대로 다른 설명이나 주석을 추가하지 마.**
            2. 판단은 `BUY` 또는 `SELL` 중 하나만 골라야 해.
            3. 응답 JSON은 반드시 아래 형식을 따라야 해:

            {
              "decision": "BUY 또는 SELL",
              "confidence": 0.0 ~ 1.0 사이의 소수 (예측 판단의 확신도),
            }
            confidence는 너의 예측이 얼마나 신뢰할 만한지를 수치로 표현해. (높을수록 확신)
            
            참고: 입력 데이터 구조는 다음과 같아
            [
              {
                "open": 42600000,
                "high": 42650000,
                "low": 42580000,
                "close": 42620000,
                "volume": 12.3,
                "value": 521000000.0
              },
              ...
            ]
            
            이제 위 30개의 캔들을 분석해서, 마지막 시점 기준으로 10분 후 가격이 오를지/내릴지 판단해줘.
            출력은 반드시 JSON만 해.
            """.trimIndent()
        )
        return contentBuilder.toString()
    }
}