package com.prakash.speaksmart.data.remote

import com.prakash.speaksmart.data.model.OpenAiRequest
import com.prakash.speaksmart.data.model.SpeechFeedbackResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class OpenAiApiService(private val client: HttpClient) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    suspend fun postOpenAiResponse(
        request: OpenAiRequest
    ): SpeechFeedbackResponse {

        val raw = client.post("responses") {
            setBody(request)
        }.bodyAsText()

        val jsonElement = json.parseToJsonElement(raw)

        val jsonString = jsonElement.jsonObject["output"]
            ?.jsonArray
            ?.firstOrNull()
            ?.jsonObject
            ?.get("content")
            ?.jsonArray
            ?.firstOrNull()
            ?.jsonObject
            ?.get("text")
            ?.jsonPrimitive
            ?.content

        require(!jsonString.isNullOrBlank()) { "OpenAI output text block was empty or missing" }

        val cleaned = cleanJson(jsonString)

        return json.decodeFromString<SpeechFeedbackResponse>(cleaned)
    }

    private fun cleanJson(raw: String): String {
        return raw
            .trim()
            .removePrefix("```json")
            .removePrefix("```")
            .removeSuffix("```")
            .trim()
    }
}