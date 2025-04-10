package com.example.geotrack.ui.tracking.state

import org.osmdroid.util.GeoPoint

sealed class TrackingIntent {
    object StartTracking : TrackingIntent()
    object StopTracking : TrackingIntent()
    object AbandonTracking : TrackingIntent()
    object TogglePause : TrackingIntent()
    data class UpdateLocation(val point: GeoPoint, val speed: Float) : TrackingIntent()
}
