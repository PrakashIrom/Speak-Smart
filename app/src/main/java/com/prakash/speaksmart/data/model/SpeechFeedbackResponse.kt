package com.prakash.speaksmart.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SpeechFeedbackResponse(
    val originalSentence: String,
    val correctedSentence: String,
    val confidenceScore: Int,
    val grammarMistakes: List<String>,
    val pronunciationTips: List<String>,
    val fluencyTips: List<String>
)