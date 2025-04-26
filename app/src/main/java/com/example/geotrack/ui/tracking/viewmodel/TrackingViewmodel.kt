package com.example.geotrack.ui.tracking.viewmodel


import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geotrack.domain.routeTracking.GeoRepository
import com.example.geotrack.domain.routeTracking.TrackInteractor
import com.example.geotrack.domain.routeTracking.model.GpxPoint
import com.example.geotrack.ui.tracking.state.TrackingIntent
import com.example.geotrack.ui.tracking.state.TrackingIntent.StopTracking
import com.example.geotrack.ui.tracking.state.TrackingState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint


class TrackingViewModel(
    private val repository: GeoRepository,
    private val trackInteractor: TrackInteractor
) : ViewModel() {
    private val _state = MutableStateFlow(TrackingState())
    val state: StateFlow<TrackingState> = _state.asStateFlow()
    private var gpxPoints: MutableList<GpxPoint> = mutableListOf()

    private var startTime: Long = 0L
    private var endTime: Long = 0L
    private var pauseOffset: Long = 0L
    private var locationJob: Job? = null
    private var timerJob: Job? = null
    fun processIntent(intent: TrackingIntent) {
        when (intent) {
            TrackingIntent.StartTracking -> startTracking()
            is StopTracking -> stopTracking(intent.mapBitmap)
            TrackingIntent.TogglePause -> togglePause()
            TrackingIntent.AbandonTracking -> abandonTracking()
            is TrackingIntent.UpdateLocation -> updateState(intent.point, intent.speed)
        }
    }

    private fun startTracking() {
        _state.update { it.copy(isTracking = true) }
        startTime = System.currentTimeMillis()
        startLocationUpdates()
    }


    private fun abandonTracking() {
        _state.update {
            it.copy(
                isTracking = false,
                geoPoints = emptyList(),
                currentSpeed = "0.0 км/ч",
                totalDistance = "0 км",
                elapsedTime = "0:00"
            )
        }
        resetTimers()
        locationJob?.cancel()
        timerJob?.cancel()
        gpxPoints.clear()
    }
    private fun startLocationUpdates() {
        locationJob = viewModelScope.launch {
            repository.getLocationUpdates()
                .collect { location ->
                    processIntent(
                        TrackingIntent.UpdateLocation(
                            GeoPoint(location.latitude, location.longitude),
                            location.speed
                        )
                    )
                    gpxPoints.add(GpxPoint(location.latitude, location.longitude, location.time))
                }
        }
        timerJob = viewModelScope.launch {
            while(state.value.isTracking) {
                delay(1000)
                _state.update {
                    it.copy(
                        elapsedTime = calculateElapsedTime()
                    )
                }
            }
        }
    }

    private fun togglePause() {
        _state.update { it.copy(isPaused = !it.isPaused) }
        updateTimers()
    }

    private fun updateState(point: GeoPoint, speed: Float) {
        if (!_state.value.isPaused) {
            val newPoints = _state.value.geoPoints + point
            val newSpeed = "%.1f км/ч".format(speed * 3.6f)
            val newDistance = calculateTotalDistance(newPoints)

            _state.update {
                it.copy(
                    geoPoints = newPoints,
                    currentSpeed = newSpeed,
                    totalDistance = newDistance,
                )
            }
        }
    }

    private fun stopTracking(bitmap: Bitmap?) {
        viewModelScope.launch {
            val points = _state.value.geoPoints
            if (points.isNotEmpty()) {
                trackInteractor.saveTrack(gpxPoints = gpxPoints, geoPoints = points, startTime = startTime, endTime = endTime, image = bitmap)
            }
            timerJob?.cancel()
            gpxPoints.clear()
            resetTimers()
            locationJob?.cancel()
            _state.update {
                it.copy(
                    isTracking = false,
                    geoPoints = emptyList(),
                    currentSpeed = "0.0 км/ч",
                    totalDistance = "0 км",
                    elapsedTime = "0:00"
                )
            }
        }
    }

    private fun calculateTotalDistance(points: List<GeoPoint>): String {
        if (points.size < 2) return "0 км"
        var distance = 0.0
        for (i in 1 until points.size) {
            distance += points[i-1].distanceToAsDouble(points[i])
        }
        return "%.2f км".format(distance / 1000)
    }

    private fun calculateAverageSpeed(distanceKm: Double, durationMs: Long): Double {
        if (durationMs == 0L) return 0.0
        val hours = durationMs.toDouble() / 1000 / 3600
        return distanceKm / hours
    }

    private fun calculateElapsedTime(): String {
        val currentTime = if (_state.value.isPaused) pauseOffset else System.currentTimeMillis() - startTime
        val minutes = (currentTime / 1000) / 60
        val seconds = (currentTime / 1000) % 60
        return "%d:%02d".format(minutes, seconds)
    }

    private fun updateTimers() {
        if (_state.value.isPaused) {
            pauseOffset = System.currentTimeMillis() - startTime
        } else {
            startTime = System.currentTimeMillis() - pauseOffset
        }
    }

    private fun resetTimers() {
        startTime = 0L
        pauseOffset = 0L
    }

}
