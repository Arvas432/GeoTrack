package com.example.geotrack.util

import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint

object MapUtils {

    fun createSquareBoundingBox(points: List<GeoPoint>): BoundingBox {
        if (points.isEmpty()) throw IllegalArgumentException("Points list cannot be empty")
        var minLat = Double.MAX_VALUE
        var maxLat = -Double.MAX_VALUE
        var minLon = Double.MAX_VALUE
        var maxLon = -Double.MAX_VALUE

        for (point in points) {
            minLat = minOf(minLat, point.latitude)
            maxLat = maxOf(maxLat, point.latitude)
            minLon = minOf(minLon, point.longitude)
            maxLon = maxOf(maxLon, point.longitude)
        }

        val centerLat = (minLat + maxLat) / 2
        val centerLon = (minLon + maxLon) / 2

        val latDelta = maxLat - minLat
        val lonDelta = maxLon - minLon

        val maxDelta = maxOf(latDelta, lonDelta) / 2

        return BoundingBox(
            centerLat + maxDelta,
            centerLon + maxDelta,
            centerLat - maxDelta,
            centerLon - maxDelta
        )
    }
}