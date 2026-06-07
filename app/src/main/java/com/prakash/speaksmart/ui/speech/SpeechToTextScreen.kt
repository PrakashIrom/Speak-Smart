package com.prakash.speaksmart.ui.speech

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SpeechScreen(viewModel: SpeechViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current


    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) viewModel.onAction(SpeechAction.ToggleListening)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = state.text.ifEmpty { "Tap to speak" })

        if (state.error != null) {
            Text(text = state.error!!, color = Color.Red)
        }

        Button(
            onClick = {
                when {
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        viewModel.onAction(
                            SpeechAction.ToggleListening
                        )
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
                    "Idle"
            )
        }
    }
}