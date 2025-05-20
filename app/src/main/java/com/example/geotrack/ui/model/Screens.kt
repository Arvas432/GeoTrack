package com.example.geotrack.ui.model

sealed class Screens(val route : String) {
    data object Authorization : Screens("auth_route")
    data object Tracking : Screens("tracking_route")
    data object Feed : Screens("feed_route")
    data object Profile : Screens("profile_route")
    data object ProfileCreation : Screens("profile_creating_route")
    data object Settings : Screens("settings_route")
}