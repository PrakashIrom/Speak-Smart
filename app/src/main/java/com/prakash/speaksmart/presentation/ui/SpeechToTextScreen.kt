package com.prakash.speaksmart.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SpeechScreen(viewModel: SpeechViewModel = koinViewModel()) {

    val state by viewModel.state.collectAsState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.onAction(SpeechAction.ToggleListening)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "SpeakSmart AI Coach",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 🎤 TRANSCRIPT BOX
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = "You said:",
                    style = MaterialTheme.typography.labelMedium
                )

                Text(
                    text = state.text.ifEmpty { "Start speaking..." },
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        uiState.error?.let { error ->
            Text(
                text = error,
                color = Color.Red
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // ⏳ LOADING STATE
        if (uiState.isLoading) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(8.dp))
            Text("Analyzing your speech...")
        }

        // ✅ AI RESPONSE UI
        uiState.feedback?.let { feedback ->

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF5F5F5)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text("Corrected Sentence:", fontWeight = FontWeight.Bold)
                    Text(feedback.correctedSentence)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Confidence: ${feedback.confidenceScore}%")

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Grammar Mistakes:")
                    feedback.grammarMistakes.forEach {
                        Text("• $it")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Pronunciation Tips:")
                    feedback.pronunciationTips.forEach {
                        Text("• $it")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Fluency Tips:")
                    feedback.fluencyTips.forEach {
                        Text("• $it")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🎤 MIC BUTTON
        Button(
            onClick = {
                when {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        viewModel.onAction(SpeechAction.ToggleListening)
                    }

                    else -> {
                        permissionLauncher.launch(
                            Manifest.permission.RECORD_AUDIO
                        )
                    }
                }
            }
        ) {
            Text(
                text = if (state.isListening)
                    "🎤 Listening..."
                else
                    "Start Speaking"
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 🤖 ANALYZE BUTTON
        Button(
            onClick = {
                viewModel.analyzeSpeech(state.text)
            },
            enabled = state.text.isNotBlank() && !uiState.isLoading
        ) {
            Text("Analyze Speech")
        }
    }
}