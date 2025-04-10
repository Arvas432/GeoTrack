package com.example.geotrack.util

import com.example.geotrack.domain.routeTracking.model.GpxPoint
import org.osmdroid.util.GeoPoint
import java.time.Instant

class GpxConverter {
    fun calculateTotalDistance(points: List<GeoPoint>): Double {
        if (points.size < 2) return 0.0
        var distance = 0.0
        for (i in 1 until points.size) {
            distance += points[i-1].distanceToAsDouble(points[i])
        }
        return distance / 1000 // convert to kilometers
    }

    fun calculateAverageSpeed(distanceKm: Double, durationMs: Long): Double {
        if (durationMs == 0L) return 0.0
        val hours = durationMs.toDouble() / 1000 / 3600
        return distanceKm / hours
    }

    fun convertToGpx(points: List<GpxPoint>, name: String): String {
        return """<?xml version="1.0" encoding="UTF-8"?>
            <gpx version="1.1" creator="MyTrackingApp">
                <trk>
                    <name>$name</name>
                    <trkseg>
                        ${points.joinToString("\n") { point ->
            "<trkpt lat=\"${point.latitude}\" lon=\"${point.longitude}\">" +
                    "<time>${Instant.ofEpochMilli(point.timestamp)}</time>" +
                    "</trkpt>"
        }}
                    </trkseg>
                </trk>
            </gpx>""".trimIndent()
    }
}