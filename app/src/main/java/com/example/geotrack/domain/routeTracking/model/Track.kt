package com.example.geotrack.domain.routeTracking.model

import android.graphics.Bitmap
import java.time.Instant
import kotlin.time.Duration

data class Track(
    val id: Long,
    val name: String,
    val date: Instant,
    val duration: Duration,
    val distance: Double,
    val averageSpeed: Double,
    val gpxData: String?,
    val likes: Int?,
    val image: Bitmap?
)
