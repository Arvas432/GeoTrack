package com.example.geotrack.ui.tracking.state

import android.graphics.Bitmap
import org.osmdroid.util.GeoPoint

sealed class TrackingIntent {
    object StartTracking : TrackingIntent()
    data class StopTracking(val mapBitmap: Bitmap?) : TrackingIntent()
    object ToggleTrackingEndMenu: TrackingIntent()
    object AbandonTracking : TrackingIntent()
    object TogglePause : TrackingIntent()
    data class UpdateLocation(val point: GeoPoint, val speed: Float) : TrackingIntent()
}
