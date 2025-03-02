package com.example.geotrack.domain.tracking

import kotlinx.coroutines.flow.Flow
import org.osmdroid.util.GeoPoint

interface LocationRepository {
    fun getLocationUpdates(): Flow<Pair<GeoPoint, Float>>
    suspend fun calculateDistance(points: List<GeoPoint>): Double
}