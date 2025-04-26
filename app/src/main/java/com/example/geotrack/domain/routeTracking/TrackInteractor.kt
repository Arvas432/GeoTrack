package com.example.geotrack.domain.routeTracking

import android.graphics.Bitmap
import com.example.geotrack.domain.routeTracking.model.GpxPoint
import org.osmdroid.util.GeoPoint

interface TrackInteractor {
    suspend fun saveTrack(gpxPoints: List<GpxPoint>,
                          geoPoints: List<GeoPoint>,
                          image: Bitmap?,
                          startTime: Long,
                          endTime: Long)
}