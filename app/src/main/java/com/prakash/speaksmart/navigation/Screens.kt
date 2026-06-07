package com.prakash.speaksmart.navigation

sealed class Screen(val route: String) {

    data object Home : Screen("home")

    data object Speech : Screen("speech")

    data object Feedback : Screen("feedback")

    data object Scenario : Screen("scenario")
}