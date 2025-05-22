package com.example.geotrack.domain.public_tracks

import android.graphics.Bitmap
import com.example.geotrack.domain.Route
import com.example.geotrack.domain.User
import java.time.Instant
import kotlin.time.Duration

data class Post(
    val serverId: Long? = null,
    val username: String,
    val name: String,
    val date: Instant,
    val duration: Duration,
    val distance: Double,
    val averageSpeed: Double,
    val gpxData: String?,
    val likes: Int?,
    val liked: Boolean = false,
    val image: Bitmap?
)
