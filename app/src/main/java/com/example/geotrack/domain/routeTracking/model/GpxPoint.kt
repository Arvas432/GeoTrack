package com.example.geotrack.domain.routeTracking.model

data class GpxPoint(
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long = System.currentTimeMillis()
)