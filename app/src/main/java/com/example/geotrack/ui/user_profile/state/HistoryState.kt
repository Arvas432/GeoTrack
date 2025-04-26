package com.example.geotrack.ui.user_profile.state

import com.example.geotrack.domain.routeTracking.model.Track

data class HistoryState (
    val tracks: List<Track> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)