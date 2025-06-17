package com.example.geotrack.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class TrackDto(
    val serverId: Long,
    val localId: Long,
    val name: String,
    val date: String,
    val duration: Long,
    val distance: Double,
    val averageSpeed: Double,
    val gpxData: String? = null,
    val image_base64: String? = null,
    val likes: Int? = null
)
