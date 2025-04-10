package com.example.geotrack.domain.routeTracking

import android.content.Context
import android.location.Location
import kotlinx.coroutines.flow.Flow

interface GeoRepository {
    fun getLocationUpdates() : Flow<Location>
}