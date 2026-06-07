package com.prakash.speaksmart.data.model

data class SpeechFeedback(
    val correctedSentence: String,
    val confidenceScore: Int,
    val mistakes: List<String>,
    val tips: List<String>
)