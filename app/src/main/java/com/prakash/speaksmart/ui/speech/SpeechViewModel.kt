package com.prakash.speaksmart.ui.speech

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prakash.speaksmart.BuildConfig
import com.prakash.speaksmart.data.model.OpenAiRequest
import com.prakash.speaksmart.data.model.SpeechFeedbackResponse
import com.prakash.speaksmart.domain.repository.SpeechRecognizerRepository
import com.prakash.speaksmart.domain.usecase.AnalyzeSpeechUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SpeechUiState(
    val transcript: String = "",
    val isListening: Boolean = false,
    val isLoading: Boolean = false,
    val feedback: SpeechFeedbackResponse? = null,
    val error: String? = null
)

class SpeechViewModel(
    private val repository: SpeechRecognizerRepository,
    private val analyzeSpeechUseCase: AnalyzeSpeechUseCase
) : ViewModel() {

    val state = repository.state
    val _uiState = MutableStateFlow(SpeechUiState())
    val uiState = _uiState.asStateFlow()

    fun analyzeSpeech(transcript: String) {
        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            try {

                val prompt = """
                You are an English speaking coach.

                Analyze this sentence:
                "$transcript"

                Return ONLY valid JSON:

                {
                  "originalSentence": "$transcript",
                  "correctedSentence": "",
                  "confidenceScore": 0,
                  "grammarMistakes": [],
                  "pronunciationTips": [],
                  "fluencyTips": []
                }
            """.trimIndent()

                val request = OpenAiRequest(
                    model = BuildConfig.OPENAI_MODEL,
                    input = prompt
                )

                val response = analyzeSpeechUseCase.invoke(request)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        feedback = response
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Something went wrong"
                    )
                }
            }
        }
    }
    fun onAction(action: SpeechAction) {
        when (action) {
            SpeechAction.ToggleListening -> {
                if (state.value.isListening) repository.stopListening()
                else repository.startListening()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.destroy()
    }
}

sealed class SpeechAction {
    object ToggleListening : SpeechAction()
}