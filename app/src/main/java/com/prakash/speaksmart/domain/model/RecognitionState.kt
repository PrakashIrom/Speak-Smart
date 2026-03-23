package com.prakash.speaksmart.domain.model

data class RecognitionState(
    val text: String = "",
    val isListening: Boolean = false,
    val error: String? = null
)