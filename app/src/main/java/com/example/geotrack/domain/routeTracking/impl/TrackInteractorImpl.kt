package com.example.geotrack.domain.routeTracking.impl

import android.graphics.Bitmap
import android.util.Log
import com.example.geotrack.domain.routeTracking.TrackInteractor
import com.example.geotrack.domain.routeTracking.TrackRepository
import com.example.geotrack.domain.routeTracking.model.GpxPoint
import com.example.geotrack.domain.routeTracking.model.Track
import com.example.geotrack.util.GpxConverter
import org.osmdroid.util.GeoPoint
import java.time.Instant
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class TrackInteractorImpl(
    private val trackRepository: TrackRepository,
    private val geoConverter: GpxConverter
) :
    TrackInteractor {
    private fun generateTrackName(timestamp: Long): String {
        return "Track ${Instant.ofEpochMilli(timestamp)}"
    }

    override suspend fun saveTrack(
        gpxPoints: List<GpxPoint>,
        geoPoints: List<GeoPoint>,
        name: String,
        image: Bitmap?,
        startTime: Long,
        endTime: Long
    ) {
        require(gpxPoints.isNotEmpty() && geoPoints.isNotEmpty()) { "Cannot save empty track" }
        val totalDistance = geoConverter.calculateTotalDistance(geoPoints)
        val duration = endTime - startTime
        Log.i("DURATION", duration.toString())
        val averageSpeed = geoConverter.calculateAverageSpeed(totalDistance, duration)
        val gpxData = geoConverter.convertToGpx(gpxPoints, "Track")

        val track = Track(
            localDbId = System.currentTimeMillis(),
            name = name,
            date = Instant.ofEpochMilli(startTime),
            duration = duration.toDuration(unit = DurationUnit.MILLISECONDS),
            distance = totalDistance,
            averageSpeed = averageSpeed,
            gpxData = gpxData,
            likes = 0,
            image = image
        )

        trackRepository.saveTrack(track)

    }
}