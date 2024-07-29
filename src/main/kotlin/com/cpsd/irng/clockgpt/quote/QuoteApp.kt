package com.cpsd.irng.clockgpt.quote


import com.cpsd.irng.clockgpt.common.ClockClient
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import org.jsoup.Jsoup
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class QuoteApp(private val clockClient: ClockClient, private val objectMapper: ObjectMapper) {
    private val meteoClient = HttpClient()
    val log : Logger = LoggerFactory.getLogger(ClockClient::class.java)

    init {
        println("Quote service started")
    }

    suspend fun quote() : String {
        val client = HttpClient()
        val content = client.get("https://www.ephemeride.com/ephemeride/citation/15/citations.html").body<String>()
        val element =  Jsoup.parse(content).selectXpath("//*[@id=\"pContent\"]/table/tbody/tr[2]/td/table/tbody/tr[2]/td[3]/table/tbody/tr/td[2]").random().text()

        log.info("quote : $element")

        return element
    }

//    @Scheduled(fixedRate = 300_000)
    fun scheduledSendMessage() {
        runBlocking {
            clockClient.sendMessage("quote", quote(),  duration = 30)
        }
    }

}

