package com.prakash.speaksmart.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SpeechFeedbackResponse(
    val originalSentence: String,
    val correctedSentence: String,
    val confidenceScore: Double,
    val grammarMistakes: List<GrammarMistake>,
    val pronunciationTips: List<Tip>,
    val fluencyTips: List<Tip>
)

@Serializable
data class GrammarMistake(
    val mistake: String,
    val explanation: String
)

@Serializable
data class Tip(
    val tip: String
)