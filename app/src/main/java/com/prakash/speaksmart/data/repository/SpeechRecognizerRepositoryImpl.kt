package com.prakash.speaksmart.data.repository

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.prakash.speaksmart.domain.model.RecognitionState
import com.prakash.speaksmart.domain.repository.SpeechRecognizerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

class SpeechRecognizerRepositoryImpl(
    private val context: Context,
) : SpeechRecognizerRepository, RecognitionListener {

    private val recognizer: SpeechRecognizer? =
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            SpeechRecognizer.createSpeechRecognizer(context)
        } else null

    private val _state = MutableStateFlow(RecognitionState())
    override val state = _state.asStateFlow()

    private val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
    }

    init {
        recognizer?.setRecognitionListener(this)
    }

    override fun startListening() {
        if (recognizer == null) {
            _state.update {
                it.copy(error = "Speech recognition not available")
            }
            return
        }

        _state.update { it.copy(isListening = true, error = null) }
        recognizer.startListening(intent)
    }

    override fun stopListening() {
        _state.update { it.copy(isListening = false) }
        recognizer?.stopListening()
    }

    override fun destroy() {
        recognizer?.destroy()
    }

    override fun onResults(results: Bundle?) {
        val matches = results
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

        val text = matches?.firstOrNull().orEmpty()

        _state.update {
            it.copy(
                text = text,
                isListening = false,
                isFinalResult = true
            )
        }
    }

    override fun onPartialResults(partialResults: Bundle?) {
        val matches = partialResults
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

        val text = matches?.firstOrNull().orEmpty()

        _state.update {
            it.copy(text = text)
        }
    }

    override fun onError(error: Int) {
        _state.update {
            it.copy(
                error = getErrorMessage(error),
                isListening = false
            )
        }
    }

    private fun getErrorMessage(error: Int): String {
        return when (error) {
            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
            SpeechRecognizer.ERROR_CLIENT -> "Client error"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Permission denied"
            SpeechRecognizer.ERROR_NETWORK -> "Network error"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
            SpeechRecognizer.ERROR_NO_MATCH -> "No speech recognized"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognizer busy"
            SpeechRecognizer.ERROR_SERVER -> "Server error"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
            else -> "Unknown error"
        }
    }

    override fun onReadyForSpeech(params: Bundle?) {}
    override fun onBeginningOfSpeech() {}
    override fun onRmsChanged(rmsdB: Float) {}
    override fun onBufferReceived(buffer: ByteArray?) {}
    override fun onEndOfSpeech() {}
    override fun onEvent(eventType: Int, params: Bundle?) {}
}