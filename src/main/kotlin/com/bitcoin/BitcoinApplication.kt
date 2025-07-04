package com.bitcoin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BitcoinApplication

fun main(args: Array<String>) {
	runApplication<BitcoinApplication>(*args)
}
