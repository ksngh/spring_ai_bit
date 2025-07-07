package com.bitcoin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class BitcoinApplication

fun main(args: Array<String>) {
	runApplication<BitcoinApplication>(*args)
}
