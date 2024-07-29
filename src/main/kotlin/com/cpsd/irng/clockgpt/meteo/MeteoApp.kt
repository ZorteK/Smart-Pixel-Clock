package com.cpsd.irng.clockgpt.meteo


import com.cpsd.irng.clockgpt.common.ClockClient
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.time.ZonedDateTime

@Service
class MeteoApp(private val clockClient: ClockClient, private val objectMapper : ObjectMapper) {
    private val meteoClient = HttpClient()
    val log : Logger = LoggerFactory.getLogger(ClockClient::class.java)

    init {
        println("Meteo service started")
    }

    @Scheduled(fixedRate = 300_000)
    fun scheduledSendMessage() {
        runBlocking {
            val meteo = getWeather()
            var text = "${meteo.temperature_2m} °C"
            if(meteo.precipitation_probability>30){
                text+=", pluie : ${meteo.precipitation_probability} %"
            }
            val icon = getIcon(meteo)
            clockClient.sendMessage("meteo",text,icon = icon, duration = 10)
        }
    }

    private fun getIcon(meteo: WeatherData): String {
       return when {
           meteo.precipitation_probability > 70 ->  "72"
           meteo.weather_code  == 3-> return "2282"
           meteo.weather_code  == 2-> return "876"
           meteo.weather_code  in 0..1-> return "2283"
           else -> return "72"
       }
    }

    private suspend fun getWeather(): WeatherData {
        val dataRaw =
            meteoClient.get("https://api.open-meteo.com/v1/forecast?latitude=48.8902&longitude=2.1746&hourly=temperature_2m,precipitation_probability,rain,weather_code&forecast_days=1")
                .body<String>()
        val data  = objectMapper.readValue(dataRaw, WeatherDataRaw::class.java)
        val currentUtcTime = ZonedDateTime.now(ZoneId.of("UTC"))
        val hour = currentUtcTime.hour
        val temperature = data.hourly.temperature_2m[hour]
        val precipitation_probability = data.hourly.precipitation_probability[hour]
        val rain = data.hourly.rain[hour]
        val weather_code = data.hourly.weather_code[hour]

        return WeatherData(temperature, precipitation_probability, rain, weather_code)
    }





    data class WeatherData(
        val temperature_2m: Double,
        val precipitation_probability: Double,
        val rain: Double,
        val weather_code: Int
    )

    data class WeatherDataRaw(val hourly: HourlyData)
    data class HourlyData(
        val temperature_2m: List<Double>,
        val precipitation_probability: List<Double>,
        val rain: List<Double>,
        val weather_code: List<Int>
    )
}

