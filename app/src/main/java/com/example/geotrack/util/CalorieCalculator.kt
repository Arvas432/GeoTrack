package com.example.geotrack.util

import android.util.Log

object CalorieCalculator {
    fun calculateCaloriesBurned(
        distanceKm: Double,
        avgSpeedKmH: Double,
        timeHours: Double,
        weightKg: Double,
    ): Double {
        val met = getMETForSpeed(avgSpeedKmH)
        Log.i("met", met.toString())
        return met * weightKg * timeHours
    }

    private fun getMETForSpeed(speedKmH: Double): Double {
        return when {
            speedKmH < 10.0 -> 4.0
            speedKmH < 15.0 -> 6.0
            speedKmH < 20.0 -> 8.0
            else -> 10.0
        }
    }
}