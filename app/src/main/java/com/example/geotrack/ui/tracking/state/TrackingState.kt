package com.example.geotrack.ui.tracking.state

import org.osmdroid.util.GeoPoint

data class TrackingState(
    val geoPoints: List<GeoPoint> = emptyList(),
    val currentSpeed: String = "0.0 км/ч",
    val totalDistance: String = "0 км",
    val elapsedTime: String = "0:00",
    val isPaused: Boolean = false,
    val isTracking: Boolean = false
)
