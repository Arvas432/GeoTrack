package com.example.geotrack.domain.profile.model

import android.graphics.Bitmap

data class UserProfile(
    val id: Int = 0,
    val name: String,
    val height: Int,
    val weight: Int,
    val completedRoutes: Int = 0,
    val profileImageBitmap: Bitmap? = null
)
