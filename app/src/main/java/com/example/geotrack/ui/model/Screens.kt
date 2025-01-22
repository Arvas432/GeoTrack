package com.example.geotrack.ui.model

sealed class Screens(val route : String) {
    object Authorization : Screens("auth_route")
    object Tracking : Screens("tracking_route")
    object Feed : Screens("feed_route")
    object Profile : Screens("profile_route")
    object Settings : Screens("settings_route")
}