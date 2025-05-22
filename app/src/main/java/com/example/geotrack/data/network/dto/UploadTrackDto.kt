package com.example.geotrack.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class UploadTrackDto(
    val localId: Long,
    val name: String,
    val date: String,
    val duration: Long,
    val distance: Double,
    val averageSpeed: Double,
    val gpxData: String?,
    val imageBase64: String?
)
