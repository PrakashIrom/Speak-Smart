package com.prakash.speaksmart.ui.speech

import android.Manifest
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

@Composable
fun SpeechScreen(viewModel: SpeechViewModel, modifier:Modifier) {
    val state by viewModel.state.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()    ) { isGranted ->
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

        Button(onClick = {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }) {
            Text(if (state.isListening) "Stop" else "Start")
        }
    }
}