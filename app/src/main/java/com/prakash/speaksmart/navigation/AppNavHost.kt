package com.prakash.speaksmart.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prakash.speaksmart.ui.speech.SpeechScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Speech.route) {
        composable(Screen.Speech.route) {
            SpeechScreen()
        }
    }
}