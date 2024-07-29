package com.cpsd.irng.clockgpt.common

import io.ktor.client.*
import io.ktor.client.request.*
import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/*
 * Copyright 2023 CITEOS PARIS SOLUTIONS DIGITALES
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *//**
 * @author user()
 */
@Service
class ClockClient {
    suspend fun sendMessage(
        appName: String,
        text: String,
        icon: String? = null,
        rainbow: Boolean = false,
        duration: Int
    ) {
        val log : Logger = LoggerFactory.getLogger(ClockClient::class.java)
        val client = HttpClient()
        val payload = """
            {
              "text": "$text",
              "icon": "${icon ?: ""}",
              "rainbow": $rainbow,
              "duration": $duration
            }
        """.trimIndent()
        val url = "http://10.0.0.10/api/custom?name=$appName"
        log.info("sending message $payload to $url" )
        client.post(url) {
            setBody(
                payload
            )
        }
    }

}
