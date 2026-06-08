package com.prakash.speaksmart.presentation.ui.practice

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.prakash.speaksmart.data.model.GrammarMistake
import com.prakash.speaksmart.data.model.SpeechFeedbackResponse
import com.prakash.speaksmart.data.model.Tip
import com.prakash.speaksmart.presentation.theme.SpeakSmartTheme
import com.prakash.speaksmart.presentation.ui.SpeechAction
import com.prakash.speaksmart.presentation.ui.SpeechUiState
import com.prakash.speaksmart.presentation.ui.SpeechViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreen(viewModel: SpeechViewModel = koinViewModel()) {
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Practice With SpeakSmart",
                        style = SpeakSmartTheme.HeadlineMd.copy(
                            color = SpeakSmartTheme.EmeraldGreen,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = SpeakSmartTheme.Surface
                )
            )
        },
        containerColor = SpeakSmartTheme.Surface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // Dynamic Middle Content Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when {
                    uiState.isLoading -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CircularProgressIndicator(
                                color = SpeakSmartTheme.CoolBlue,
                                strokeWidth = 4.dp
                            )
                            Text(
                                text = "Analyzing your speech...",
                                style = SpeakSmartTheme.LabelMd,
                                color = SpeakSmartTheme.CoolBlue
                            )
                        }
                    }

                    state.isListening -> {
                        AudioVisualizerWave()
                    }

                    uiState.feedback != null -> {
                        FeedbackContentList(data = uiState.feedback!!)
                    }

                    uiState.error != null -> {
                        Text(
                            text = uiState.error!!,
                            style = SpeakSmartTheme.BodyMd,
                            color = SpeakSmartTheme.PulsingCrimson,
                            textAlign = TextAlign.Center
                        )
                    }

                    else -> {
                        // Display clean prompt text if transcript is empty
                        Text(
                            text = state.text.ifEmpty { "Tap the mic and start speaking naturally..." },
                            style = SpeakSmartTheme.BodyLg,
                            color = SpeakSmartTheme.OnSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }

            // Bottom Control Center
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {

                // 🎤 Premium Core Mic Controller
                MicControlButton(
                    isListening = state.isListening,
                    onClick = {
                        if (state.isListening) {
                            // 1. Stop Recording Voice
                            viewModel.onAction(SpeechAction.ToggleListening)
                            // 2. Automate triggering AI analysis right away for maximum speed score!
                            if (state.text.isNotBlank()) {
                                viewModel.analyzeSpeech(state.text)
                            }
                        } else {
                            // Check microphone safety runtime access permissions
                            val hasPermission = ContextCompat.checkSelfPermission(
                                context, Manifest.permission.RECORD_AUDIO
                            ) == PackageManager.PERMISSION_GRANTED

                            if (hasPermission) {
                                viewModel.onAction(SpeechAction.ToggleListening)
                            } else {
                                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            }
                        }
                    }
                )

                if (!state.isListening && !uiState.isLoading && state.text.isNotBlank()) {
                    Row(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(SpeakSmartTheme.SurfaceContainerHigh)
                            .clickable { viewModel.analyzeSpeech(state.text) }
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Magic AI Insight Icon",
                            tint = SpeakSmartTheme.EmeraldGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Analyze Current Speech",
                            style = SpeakSmartTheme.LabelMd,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreenContent(
    uiState: SpeechUiState,
    onMicButtonClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Practice With SpeakSmart",
                        style = SpeakSmartTheme.HeadlineMd.copy(
                            color = SpeakSmartTheme.EmeraldGreen,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = SpeakSmartTheme.Surface
                )
            )
        },
        containerColor = SpeakSmartTheme.Surface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator(
                            color = SpeakSmartTheme.CoolBlue,
                            strokeWidth = 4.dp
                        )
                    }

                    uiState.isListening -> {
                        AudioVisualizerWave()
                    }

                    uiState.feedback != null -> {
                        FeedbackContentList(data = uiState.feedback)
                    }

                    uiState.error != null -> {
                        Text(
                            text = uiState.error,
                            style = SpeakSmartTheme.BodyMd,
                            color = SpeakSmartTheme.PulsingCrimson,
                            textAlign = TextAlign.Center
                        )
                    }

                    else -> {
                        Text(
                            text = "Tap the mic and say something in English...",
                            style = SpeakSmartTheme.BodyLg,
                            color = SpeakSmartTheme.OnSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                MicControlButton(
                    isListening = uiState.isListening,
                    onClick = onMicButtonClick
                )
            }
        }
    }
}

@Composable
fun AudioVisualizerWave() {
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "pulse"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(SpeakSmartTheme.CoolBlue.copy(alpha = 0.15f * scale)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(SpeakSmartTheme.CoolBlue.copy(alpha = 0.3f * scale))
            )
        }
        Text(
            text = "Listening to your voice...",
            style = SpeakSmartTheme.LabelMd,
            color = SpeakSmartTheme.CoolBlue
        )
    }
}

@Composable
fun MicControlButton(
    isListening: Boolean,
    onClick: () -> Unit
) {
    val buttonColor =
        if (isListening) SpeakSmartTheme.PulsingCrimson else SpeakSmartTheme.EmeraldGreen
    val iconImage = if (isListening) Icons.Default.Stop else Icons.Default.Mic
    val iconTint = if (isListening) Color.Black else Color.White

    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(buttonColor)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = iconImage,
            contentDescription = "Microphone Toggle Button",
            tint = iconTint,
            modifier = Modifier.size(36.dp)
        )
    }
}

// --- STATIC ANDROID STUDIO PREVIEW GENERATOR ---
@Preview(showBackground = true, backgroundColor = 0xFF131313)
@Composable
fun FeedbackPreview() {
    val mockFeedback = SpeechFeedbackResponse(
        originalSentence = "I go to movie theater with my friends yesterday night.",
        correctedSentence = "I went to the movie theater with my friends last night.",
        confidenceScore = 0.88,
        grammarMistakes = listOf(
            GrammarMistake(
                "Verb Tense Error",
                "Use 'went' instead of 'go' because you are talking about yesterday."
            ),
            GrammarMistake(
                "Missing Article",
                "Add 'the' before 'movie theater' to make it grammatically natural."
            )
        ),
        pronunciationTips = listOf(
            Tip("Blend the words 'went to' together to sound smoother: pronouce it like 'wen-tu'.")
        ),
        fluencyTips = listOf(
            Tip("Saying 'last night' sounds significantly more natural than 'yesterday night' to a native speaker.")
        )
    )

    val mockUiState = SpeechUiState(
        transcript = "I go to movie theater...",
        isListening = false,
        isLoading = false,
        feedback = mockFeedback,
        error = null
    )

    PracticeScreenContent(
        uiState = mockUiState,
        onMicButtonClick = {}
    )
}