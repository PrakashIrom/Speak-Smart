package com.prakash.speaksmart.domain.repository

import com.prakash.speaksmart.data.model.OpenAiRequest
import com.prakash.speaksmart.data.model.SpeechFeedbackResponse

interface OpenAiRepo {
    suspend fun analyzeSpeech(
        request: OpenAiRequest
    ): SpeechFeedbackResponse
}