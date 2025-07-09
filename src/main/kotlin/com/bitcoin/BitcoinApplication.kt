package com.bitcoin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import reactor.blockhound.BlockHound

@SpringBootApplication
@EnableScheduling
class BitcoinApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<BitcoinApplication>(*args)
        }
    }
}