package com.cpsd.irng.clockgpt.message

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import kotlin.random.Random


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
class Messager() {

    var animaux = """
        Cocorico
        Meuh
        Beh
        Hi-han
        Cot-cot-codet
        Coin-coin
        Glou-glou
        Cot-cot-cot
        Groin-groin
        Groa
        Couac-couac
        Bê-bê
        Cot-cot
        Coa-coa
        Cric-cric
        Hou-hou
        Pia-pia
        Coincoin
        Clap-clap
        Cotcotcodet
        Cotcot
        Cocorico-cocorico
        Hihan
        Glouglou
        Cot-cot
        Hi-han-hi-han
        Cri-cri
        Cot-cot-codet-cot
        Cri
        Cocorico-cocorico-cocorico
        Houhou
        Bê
        Coa
        Cotcotcodet
        Cric
        Groa
        Hi-han-hi-han-hi-han
        Hou-hou-hou
        Cocorico-cocorico-cocorico-cocorico
        Glou
        Coa-coa-coa
        Hi-han-hi-han-hi-han-hi-han
        Groaaaa
        Cot-cot-codet-cot-cot
        Cri-cri-cri
        Hou
        Cocorico-cocorico-cocorico-cocorico-cocorico
    """.trimIndent()

    fun getRandomAnimal(): String {
        val lines = animaux.lines()
        return lines[Random.nextInt(lines.size)]
    }

    suspend fun sendMessage() {
        val client = HttpClient()
        val randomAnimal = getRandomAnimal()

        val body = """{
  "text": "${randomAnimal}",
  "duration": 10
}""".trimIndent()
        println("sending message $body")
        client.post("http://10.0.0.10/api/custom?name=test") {
            setBody(
                body
            )
        }
    }

   // @Scheduled(fixedRate = 10000)
    fun scheduledSendMessage() {
        runBlocking {
            sendMessage()
        }
    }
}
