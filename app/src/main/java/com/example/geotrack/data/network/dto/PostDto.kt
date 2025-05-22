package com.example.geotrack.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class PostDto(
    val id: Long,
    val username: String,
    val name: String,
    val date: String,
    val duration: Long,
    val distance: Double,
    val averageSpeed: Double,
    val gpxData: String?,
    val imageBase64: String?,
    val likes: Int,
)
