package com.prakash.speaksmart.data.repository

import com.prakash.speaksmart.data.model.OpenAiRequest
import com.prakash.speaksmart.data.model.SpeechFeedbackResponse
import com.prakash.speaksmart.data.remote.OpenAiApiService
import com.prakash.speaksmart.domain.repository.OpenAiRepo

class OpenAiRepoImpl(private val openAiApiService: OpenAiApiService) : OpenAiRepo {
    override suspend fun analyzeSpeech(request: OpenAiRequest): SpeechFeedbackResponse {
        return openAiApiService.postOpenAiResponse(request)
    }
}