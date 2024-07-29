package com.cpsd.irng.clockgpt.weekend

import com.cpsd.irng.clockgpt.common.ClockClient
import kotlinx.coroutines.runBlocking
import lombok.extern.slf4j.Slf4j
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDateTime

@Service
@Slf4j
class WeekEnd(private val clockClient: ClockClient) {

    init {
        println("WeekEnd service started")
    }

    private fun getWeekend(): String {
        val now = LocalDateTime.now()

        var nextFriday18 = now.with(DayOfWeek.FRIDAY).withHour(18).withMinute(0).withSecond(0)

        if (now.isAfter(nextFriday18)) {
            nextFriday18 = nextFriday18.plusWeeks(1)
        }

        val duration = Duration.between(now, nextFriday18)

        val days = duration.toDays()
        val hours = (duration.toHours() % 24).toString().padStart(2, '0')
        val minutes = (duration.toMinutes() % 60).toString().padStart(2, '0')
        val seconds = (duration.seconds % 60).toString().padStart(2, '0')

        return "${days}J ${hours}h${minutes}"
    }

    fun isWE(): Boolean {
        val now = LocalDateTime.now()
        return ((now.dayOfWeek == DayOfWeek.FRIDAY && now.hour >= 18) || now.dayOfWeek == DayOfWeek.SATURDAY || (now.dayOfWeek == DayOfWeek.SUNDAY) || (now.dayOfWeek == DayOfWeek.MONDAY && now.hour < 7))
    }

    @Scheduled(fixedRate = 60_000)
    fun scheduledSendMessage() {
        runBlocking {
            val icon = if (isWE()) "2867" else null
            if (isWE()) {
                clockClient.sendMessage(
                    "weekend", "C'est le ", icon = icon, rainbow = isWE(), 1
                )
                clockClient.sendMessage(
                    "weekend", "week-end !!", icon = icon, rainbow = isWE(), 1
                )
            } else {
                clockClient.sendMessage(
                    "weekend", "WE - ", icon = icon, rainbow = isWE(), 1
                )
                clockClient.sendMessage(
                    "weekend_2" , getWeekend(), icon = icon, rainbow = isWE(), 2
                )
            }

        }
    }

}
