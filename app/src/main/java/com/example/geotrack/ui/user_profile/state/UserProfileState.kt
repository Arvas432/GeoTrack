package com.example.geotrack.ui.user_profile.state

import android.graphics.Bitmap
import com.example.geotrack.domain.routeTracking.model.Track

data class UserProfileState (
    val name: String = "",
    val profileImageBitmap: Bitmap? = null,
    val completedRoutes: Int = 0,
    val tracks: List<Track> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)