package com.prakash.speaksmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.prakash.speaksmart.navigation.AppNavHost
import com.prakash.speaksmart.presentation.theme.SpeakSmartTheme

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