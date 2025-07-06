package com.bitcoin.client

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.bitcoin.dto.UpbitAccountResponse
import com.bitcoin.dto.UpbitCandleResponse
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*


inline fun <reified T> WebClient.ResponseSpec.bodyToTypedMono(): Mono<T> {
    return this.bodyToMono(object : ParameterizedTypeReference<T>() {})
}

@Component
class UpbitClient {

    @Value("\${upbit.secret-key}")
    private lateinit var secretKey: String

    @Value("\${upbit.access-key}")
    private lateinit var accessKey: String

    private val webClient = WebClient.builder()
        .baseUrl("https://api.upbit.com")
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build()

    suspend fun getCurrent5MinutesData(): List<UpbitCandleResponse> {
        return webClient.get()
            .uri("/v1/candles/minutes/5?market=KRW-BTC&count=288")
            .retrieve()
            .bodyToTypedMono<List<UpbitCandleResponse>>()
            .awaitSingle()
    }

    suspend fun getAvailableKrw(): String {
        val headers = makeUpbitAuthHeader(emptyMap())
        val response = webClient.get()
            .uri("/v1/accounts")
            .headers { it.setAll(headers) }
            .retrieve()
            .bodyToMono<List<UpbitAccountResponse>>()
            .awaitSingle()

        return response.find { it.currency == "KRW" }?.balance ?: "0.0"
    }

    suspend fun buyAllKrwBalance(): String {
        val krwBalance = getAvailableKrw()

        val flooredPrice = krwBalance.toBigDecimal()
            .setScale(0, RoundingMode.DOWN) // 소수점 절삭
            .divideToIntegralValue(BigDecimal(1000)) // 1000으로 나눈 몫만
            .multiply(BigDecimal(1000))
            .subtract(BigDecimal(1000))// 다시 1000을 곱해 천 단위 절삭

        val price = flooredPrice.stripTrailingZeros().toPlainString()

        val params = mapOf(
            "market" to "KRW-BTC",
            "ord_type" to "price",
            "price" to price,
            "side" to "bid"
        )
        println("📦 [DEBUG] Body Params: $params")
        val headers = makeUpbitAuthHeader(params)

        return try {
            webClient.post()
                .uri("/v1/orders")
                .headers { it.setAll(headers) }
                .bodyValue(params)
                .retrieve()
                .onStatus({ it.is4xxClientError }) { response ->
                    response.bodyToMono(String::class.java).map { body ->
                        throw RuntimeException("❌ 4xx 에러 발생: ${response.statusCode()} - $body")
                    }
                }
                .onStatus({ it.is5xxServerError }) { response ->
                    response.bodyToMono(String::class.java).map { body ->
                        throw RuntimeException("❌ 5xx 서버 에러 발생: ${response.statusCode()} - $body")
                    }
                }
                .bodyToMono(String::class.java)
                .awaitSingle()
        } catch (e: WebClientResponseException) {
            println("❗ HTTP 에러: ${e.statusCode} - ${e.responseBodyAsString}")
            throw e
        } catch (e: Exception) {
            println("❗ 예외 발생: ${e.message}")
            throw e
        }
    }

    suspend fun sellAllBtc(): String {
        val params = linkedMapOf(
            "market" to "KRW-BTC",
            "ord_type" to "market",        // 시장가
            "side" to "ask",               // 매도
            "volume" to getAvailableBtc()  // 보유 BTC 전부
        )

        val headers = makeUpbitAuthHeader(params)

        val response = webClient.post()
            .uri("/v1/orders")
            .headers { it.setAll(headers) }
            .bodyValue(params)
            .retrieve()
            .bodyToMono(String::class.java)
            .awaitSingle()

        return response
    }

    suspend fun getAvailableBtc(): String {
        val headers = makeUpbitAuthHeader(emptyMap())

        val response = webClient.get()
            .uri("/v1/accounts")
            .headers { it.setAll(headers) }
            .retrieve()
            .bodyToMono<List<UpbitAccountResponse>>()
            .awaitSingle()

        return response.find { it.currency == "BTC" }?.balance ?: "0.0"
    }

    fun makeUpbitAuthHeader(params: Map<String, String>): Map<String, String> {
        val nonce = UUID.randomUUID().toString()
        val jwtBuilder = JWT.create()
            .withClaim("access_key", accessKey)
            .withClaim("nonce", nonce)

        if (params.isNotEmpty()) {
            val queryString = params.entries
                .sortedBy { it.key }
                .joinToString("&") { "${it.key}=${it.value}" }
            println("📦 [DEBUG] Body Params: $params")
            val digest = MessageDigest.getInstance("SHA-512")
                .digest(queryString.toByteArray(StandardCharsets.UTF_8))

            val queryHash = String.format("%0128x", BigInteger(1, digest))

            jwtBuilder.withClaim("query_hash", queryHash)
                .withClaim("query_hash_alg", "SHA512")
        }

        val token = jwtBuilder.sign(Algorithm.HMAC256(secretKey))
        return mapOf("Authorization" to "Bearer $token")
    }


}