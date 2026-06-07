package com.prakash.speaksmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.prakash.speaksmart.navigation.AppNavHost
import com.prakash.speaksmart.ui.speech.SpeechScreen
import com.prakash.speaksmart.ui.theme.SpeakSmartTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeakSmartTheme {
               AppNavHost()
            }
        }
    }
}