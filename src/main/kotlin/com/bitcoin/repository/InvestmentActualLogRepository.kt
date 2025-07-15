package com.bitcoin.repository

import com.bitcoin.entity.InvestmentActualLog
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface InvestmentActualLogRepository : ReactiveCrudRepository<InvestmentActualLog,Long> {

}
