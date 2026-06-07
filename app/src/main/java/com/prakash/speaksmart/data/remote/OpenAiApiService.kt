package com.prakash.speaksmart.data.remote

import com.prakash.speaksmart.data.model.OpenAiRequest
import com.prakash.speaksmart.data.model.SpeechFeedbackResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class OpenAiApiService(private val client: HttpClient) {
    suspend fun postOpenAiResponse(request: OpenAiRequest): SpeechFeedbackResponse{
        return client.post("responses"){
            setBody(request)
        }.body<SpeechFeedbackResponse>()
    }
}