package com.example.geotrack.ui.tracking.viewmodel


import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geotrack.domain.profile.UserProfileInteractor
import com.example.geotrack.domain.routeTracking.GeoRepository
import com.example.geotrack.domain.routeTracking.TrackInteractor
import com.example.geotrack.domain.routeTracking.model.GpxPoint
import com.example.geotrack.ui.tracking.state.TrackingIntent
import com.example.geotrack.ui.tracking.state.TrackingIntent.StopTracking
import com.example.geotrack.ui.tracking.state.TrackingState
import com.example.geotrack.util.CalorieCalculator
import com.example.geotrack.util.GpxConverter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import kotlin.math.max
import kotlin.time.toDuration


class TrackingViewModel(
    private val repository: GeoRepository,
    private val trackInteractor: TrackInteractor,
    private val geoConverter: GpxConverter,
    private val profileInteractor: UserProfileInteractor
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
            TrackingIntent.ToggleTrackingEndMenu -> calculateRouteParametersBeforeSaving()
            TrackingIntent.TogglePause -> togglePause()
            TrackingIntent.AbandonTracking -> abandonTracking()
            is TrackingIntent.UpdateRouteName -> _state.update { it.copy(routeName = intent.text) }
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
            while (state.value.isTracking) {
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
            Log.i("BITMAP", bitmap.toString())
            endTime = System.currentTimeMillis()
            val points = _state.value.geoPoints
            if (points.isNotEmpty()) {
                Log.i("НАЧАЛО", startTime.toString())
                Log.i("КОНЕЦ", endTime.toString())
                trackInteractor.saveTrack(
                    gpxPoints = gpxPoints,
                    geoPoints = points,
                    name = _state.value.routeName,
                    startTime = startTime,
                    endTime = endTime,
                    image = bitmap
                )
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

    private fun calculateRouteParametersBeforeSaving() {
        viewModelScope.launch {
            val distance = geoConverter.calculateTotalDistance(_state.value.geoPoints)
            val currentDuration = System.currentTimeMillis() - startTime
            val avgSpeed = geoConverter.calculateAverageSpeed(
                distanceKm = distance,
                durationMs = currentDuration
            )
            val weight = profileInteractor.getProfile()?.weight ?: 50
            val caloriesBurned = CalorieCalculator.calculateCaloriesBurned(
                distanceKm = distance,
                avgSpeedKmH = avgSpeed,
                (currentDuration / 3600000).toDouble(),
                weightKg = weight.toDouble()
            )
            _state.update {
                it.copy(
                    totalDistance = "${Math.round(distance * 10.0) / 10.0} км",
                    avgSpeed = "${Math.round(avgSpeed * 10.0) / 10.0} км/ч",
                    calories = caloriesBurned.toString()
                )
            }
        }
    }

    private fun calculateTotalDistance(points: List<GeoPoint>): String {
        if (points.size < 2) return "0 км"
        var distance = 0.0
        for (i in 1 until points.size) {
            distance += points[i - 1].distanceToAsDouble(points[i])
        }
        return "%.2f км".format(distance / 1000)
    }


    private fun calculateElapsedTime(): String {
        val currentTime = when {
            _state.value.isPaused -> pauseOffset
            _state.value.isTracking -> System.currentTimeMillis() - startTime
            else -> 0L
        }

        val safeTime = max(currentTime, 0L)

        val totalSeconds = safeTime / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60

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
