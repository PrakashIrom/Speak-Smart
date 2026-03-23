package com.prakash.speaksmart.domain.repository

import com.prakash.speaksmart.domain.model.RecognitionState
import kotlinx.coroutines.flow.StateFlow

interface SpeechRecognizerRepository {
    fun startListening()
    fun stopListening()
    fun destroy()
    val state: StateFlow<RecognitionState>
}