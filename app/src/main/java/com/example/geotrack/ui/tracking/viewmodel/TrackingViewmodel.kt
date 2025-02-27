package com.example.geotrack.ui.tracking.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geotrack.domain.tracking.LocationRepository
import com.example.geotrack.ui.tracking.TrackingEvent
import com.example.geotrack.ui.tracking.TrackingState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import java.util.concurrent.TimeUnit


class TrackingViewModel(
    private val repository: LocationRepository
) : ViewModel() {

    private var _state by mutableStateOf<TrackingState>(TrackingState.Idle)
    val state: TrackingState get() = _state

    private var startTime: Long = 0L
    private var pauseOffset: Long = 0L
    private var trackingJob: Job? = null
    private var locationUpdatesJob: Job? = null

    fun processEvent(event: TrackingEvent) {
        when (event) {
            TrackingEvent.StartTracking -> startTracking()
            TrackingEvent.TogglePause -> togglePause()
            TrackingEvent.StopTracking -> stopTracking()
            TrackingEvent.AbandonTracking -> abandonTracking()
            is TrackingEvent.PermissionResult -> handlePermissionResult(event.granted)
            is TrackingEvent.LocationUpdate -> updateLocation(event.geoPoint, event.speed)
        }
    }

    private fun startTracking() {
        _state = TrackingState.RequestingPermission
    }

    private fun handlePermissionResult(granted: Boolean) {
        if (granted) {
            startLocationUpdates()
            startTime = System.currentTimeMillis()
            _state = TrackingState.Tracking(
                speed = "0.0",
                distance = "0.0",
                time = "00:00",
                geoPoints = emptyList(),
                isPaused = false
            )
        } else {
            _state = TrackingState.Error("Location permission required")
        }
    }

    private fun startLocationUpdates() {
        locationUpdatesJob?.cancel()
        locationUpdatesJob = viewModelScope.launch {
            repository.getLocationUpdates()
                .catch { e ->
                    _state = TrackingState.Error(e.message ?: "Location tracking error")
                }
                .collect { (geoPoint, speed) ->
                    processEvent(TrackingEvent.LocationUpdate(geoPoint, speed))
                }
        }
    }

    private fun togglePause() {
        val currentState = _state as? TrackingState.Tracking ?: return
        val isPaused = !currentState.isPaused

        if (isPaused) {
            pauseOffset = System.currentTimeMillis() - startTime
            locationUpdatesJob?.cancel()
        } else {
            startTime = System.currentTimeMillis() - pauseOffset
            startLocationUpdates()
        }

        _state = currentState.copy(isPaused = isPaused)
        updateTime()
    }

    private fun stopTracking() {
        trackingJob?.cancel()
        locationUpdatesJob?.cancel()
        _state = TrackingState.Idle
    }

    private fun abandonTracking() {
        stopTracking()
    }

    private fun updateLocation(geoPoint: GeoPoint, speed: Float) {
        viewModelScope.launch {
            val currentState = _state as? TrackingState.Tracking ?: return@launch
            val newPoints = currentState.geoPoints + geoPoint

            val distance = repository.calculateDistance(newPoints)
            val formattedDistance = "%.2f".format(distance / 1000)

            _state = currentState.copy(
                geoPoints = newPoints,
                speed = "%.1f".format(speed * 3.6),
                distance = formattedDistance
            )

            updateTime()
        }
    }

    private fun updateTime() {
        val currentState = _state as? TrackingState.Tracking ?: return
        val elapsedMillis = if (currentState.isPaused) {
            pauseOffset
        } else {
            System.currentTimeMillis() - startTime
        }

        val hours = TimeUnit.MILLISECONDS.toHours(elapsedMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedMillis) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedMillis) % 60

        val timeString = when {
            hours > 0 -> String.format("%02d:%02d:%02d", hours, minutes, seconds)
            else -> String.format("%02d:%02d", minutes, seconds)
        }

        _state = currentState.copy(time = timeString)
    }

    override fun onCleared() {
        super.onCleared()
        trackingJob?.cancel()
        locationUpdatesJob?.cancel()
    }
}