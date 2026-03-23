package com.prakash.speaksmart.ui.speech

import androidx.lifecycle.ViewModel
import com.prakash.speaksmart.domain.repository.SpeechRecognizerRepository

class SpeechViewModel(
    private val repository: SpeechRecognizerRepository
) : ViewModel() {

    val state = repository.state

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