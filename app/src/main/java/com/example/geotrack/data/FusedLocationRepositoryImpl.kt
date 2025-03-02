package com.example.geotrack.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.content.ContextCompat
import com.example.geotrack.domain.tracking.LocationRepository
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import org.osmdroid.util.GeoPoint

class FusedLocationRepository(
    private val context: Context
) : LocationRepository {
    private val client = LocationServices.getFusedLocationProviderClient(context)

    override fun getLocationUpdates(): Flow<Pair<GeoPoint, Float>> = callbackFlow {
        val request = LocationRequest.create().apply {
            interval = 5000
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    val geoPoint = GeoPoint(location.latitude, location.longitude)
                    trySend(geoPoint to location.speed)
                }
            }
        }

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            close()
            return@callbackFlow
        }

        client.requestLocationUpdates(request, callback, Looper.getMainLooper())
            .addOnFailureListener { e ->
                close(e)
            }

        awaitClose {
            client.removeLocationUpdates(callback)
        }
    }

    override suspend fun calculateDistance(points: List<GeoPoint>): Double {
        return withContext(Dispatchers.Default) {
            points.windowed(2, 1).sumOf { (prev, curr) ->
                prev.distanceToAsDouble(curr)
            }
        }
    }
}