package com.example.geotrack.ui.tracking

import org.osmdroid.util.GeoPoint

sealed class TrackingState {
    data object Idle : TrackingState()
    data object RequestingPermission : TrackingState()
    data class Tracking(
        val speed: String,
        val distance: String,
        val time: String,
        val geoPoints: List<GeoPoint>,
        val isPaused: Boolean
    ) : TrackingState()
    data class Error(val message: String) : TrackingState()
}

sealed class TrackingEvent {
    data object StartTracking : TrackingEvent()
    data object TogglePause : TrackingEvent()
    data object StopTracking : TrackingEvent()
    data object AbandonTracking : TrackingEvent()
    data class LocationUpdate(val geoPoint: GeoPoint, val speed: Float) : TrackingEvent()
    data class PermissionResult(val granted: Boolean) : TrackingEvent()
}
