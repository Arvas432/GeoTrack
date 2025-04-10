package com.example.geotrack.domain.routeTracking.impl

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
    override suspend fun saveTrack(
        gpxPoints: List<GpxPoint>,
        geoPoints: List<GeoPoint>,
        startTime: Long,
        endTime: Long
    ) {
        require(gpxPoints.isNotEmpty() && geoPoints.isNotEmpty()) { "Cannot save empty track" }
        val totalDistance = geoConverter.calculateTotalDistance(geoPoints)
        val duration = endTime - startTime
        val averageSpeed = geoConverter.calculateAverageSpeed(totalDistance, duration)
        val gpxData = geoConverter.convertToGpx(gpxPoints, "Track")

        val track = Track(
            name = generateTrackName(startTime),
            date = Instant.ofEpochMilli(startTime),
            duration = duration.toDuration(unit = DurationUnit.MINUTES),
            distance = totalDistance,
            averageSpeed = averageSpeed,
            gpxData = gpxData
        )

        trackRepository.saveTrack(track)
        trackRepository.getAllTracks().collect {
            Log.i("МАРШРУТЫ", it.toString())
        }

    }
    private fun generateTrackName(timestamp: Long): String {
        return "Track ${Instant.ofEpochMilli(timestamp)}"
    }
}