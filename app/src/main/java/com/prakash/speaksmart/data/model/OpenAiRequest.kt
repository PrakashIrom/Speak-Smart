package com.prakash.speaksmart.data.model

import kotlinx.serialization.Serializable

@Serializable
data class OpenAiRequest(
    val model: String,
    val input: String
)