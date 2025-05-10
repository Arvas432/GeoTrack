package com.example.geotrack.domain.profile.model

data class UserProfile(
    val id: Int = 0,
    val name: String,
    val height: Int,
    val weight: Int,
    val completedRoutes: Int = 0,
    val profileImageUri: String? = null
)
