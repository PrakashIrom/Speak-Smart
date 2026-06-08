package com.prakash.speaksmart.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prakash.speaksmart.presentation.ui.SpeechScreen
import com.prakash.speaksmart.presentation.ui.practice.PracticeScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Practice.route) {
        composable(Screen.Practice.route) {
            PracticeScreen()
        }
    }
}