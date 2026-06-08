package com.prakash.speaksmart.presentation.ui

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
                    error = null,
                    feedback = null
                )
            }

            try {

                val prompt = """
    You are an English speaking coach.
    
    Analyze this sentence:
    "$transcript"

    Return raw JSON text only. Do NOT wrap the response in markdown blocks like ```json.
    
    CRITICAL STRUCTURAL RULES:
    1. "grammarMistakes" must be an array of objects, where each object contains exactly two keys: "mistake" and "explanation".
    2. "pronunciationTips" must be an array of objects, where each object contains exactly one key: "tip".
    3. "fluencyTips" must be an array of objects, where each object contains exactly one key: "tip".
    4. If any array is empty, return it as []. Do not omit the keys.

    You MUST strictly follow this exact JSON structure:
    {
      "originalSentence": "I go school yesterday",
      "correctedSentence": "I went to school yesterday",
      "confidenceScore": 0.95,
      "grammarMistakes": [
        {
          "mistake": "Verb tense",
          "explanation": "The verb 'go' should be in the past tense 'went' to match 'yesterday'."
        },
        {
          "mistake": "Missing preposition",
          "explanation": "The preposition 'to' is needed before 'school'."
        }
      ],
      "pronunciationTips": [
        {
          "tip": "Make sure to pronounce the 't' sound at the end of 'went' clearly."
        }
      ],
      "fluencyTips": [
        {
          "tip": "Use simple past tense verbs when talking about actions that happened in the past."
        }
      ]
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
                        feedback = response,
                        error = null
                    )
                }

            } catch (e: java.net.UnknownHostException) {
                setError("Network error: Unable to resolve host. Please check your internet connection.")
            } catch (e: Exception) {
                setError(e.message ?: "Something went wrong")
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

    fun onSpeechResult(transcript: String) {
        _uiState.update {
            it.copy(
                transcript = transcript
            )
        }

        analyzeSpeech(transcript)
    }

    fun resetFeedback() {
        _uiState.update {
            it.copy(
                feedback = null,
                error = null,
                isLoading = false
            )
        }
    }

    fun setError(message: String) {
        _uiState.update {
            it.copy(
                isLoading = false,
                error = message
            )
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