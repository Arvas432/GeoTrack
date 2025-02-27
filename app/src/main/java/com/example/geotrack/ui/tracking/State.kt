package com.example.geotrack.ui.tracking

import org.osmdroid.util.GeoPoint

sealed class TrackingState {
    object Idle : TrackingState()
    object RequestingPermission : TrackingState()
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
    data class PermissionResult(val granted: Boolean) : TrackingEvent()
    data class LocationUpdate(val geoPoint: GeoPoint, val speed: Float) : TrackingEvent()
    object StartTracking : TrackingEvent()
    object TogglePause : TrackingEvent()
    object StopTracking : TrackingEvent()
    object AbandonTracking : TrackingEvent()
}
