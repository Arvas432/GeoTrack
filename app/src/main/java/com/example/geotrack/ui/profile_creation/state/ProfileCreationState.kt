package com.example.geotrack.ui.profile_creation.state

import android.graphics.Bitmap
import com.example.geotrack.domain.profile.model.UserProfile

data class ProfileCreationState(
    val id: Int = 0,
    val name: String = "",
    val height: String = "",
    val weight: String = "",
    val completedRoutes: Int = 0,
    val profileImageBitmap: Bitmap? = null
) {
    fun toDomainModel() = UserProfile(
        id = id,
        name = name,
        height = height.toIntOrNull() ?: 0,
        weight = weight.toIntOrNull() ?: 0,
        completedRoutes = completedRoutes,
        profileImageBitmap = profileImageBitmap
    )
}
