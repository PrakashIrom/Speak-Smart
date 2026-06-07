package com.prakash.speaksmart.domain.usecase

import com.prakash.speaksmart.data.model.OpenAiRequest
import com.prakash.speaksmart.data.model.SpeechFeedbackResponse
import com.prakash.speaksmart.domain.repository.OpenAiRepo

class AnalyzeSpeechUseCase(private val openAiRepo: OpenAiRepo) {
    suspend fun invoke(request: OpenAiRequest): SpeechFeedbackResponse {
        return openAiRepo.analyzeSpeech(request)
    }
}