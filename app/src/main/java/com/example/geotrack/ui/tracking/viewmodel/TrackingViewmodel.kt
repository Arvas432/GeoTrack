package com.example.geotrack.ui.tracking.viewmodel

import android.content.Context
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import java.util.concurrent.TimeUnit


class TrackingViewModel(
    private val repository: LocationRepository
) : ViewModel() {

    private val _state = MutableStateFlow<TrackingState>(TrackingState.Idle)
    val state: StateFlow<TrackingState> = _state.asStateFlow()

    private var startTime: Long = 0L
    private var pauseOffset: Long = 0L
    private var trackingJob: Job? = null

    init {
        _state.value = TrackingState.Tracking(
            speed = "0.0",
            distance = "0.0",
            time = "00:00",
            geoPoints = emptyList(),
            isPaused = false
        )
    }

    fun processEvent(event: TrackingEvent) {
        when (event) {
            TrackingEvent.StartTracking -> handleStartTracking()
            TrackingEvent.TogglePause -> handleTogglePause()
            TrackingEvent.StopTracking -> handleStopTracking()
            TrackingEvent.AbandonTracking -> handleAbandonTracking()
            is TrackingEvent.PermissionResult -> handlePermissionResult(event.granted)
            is TrackingEvent.LocationUpdate -> handleLocationUpdate(event.geoPoint, event.speed)
        }
    }

    private fun handleStartTracking() {
        _state.value = TrackingState.RequestingPermission
    }

    private fun handlePermissionResult(granted: Boolean) {
        _state.value = if (granted) {
            startTime = System.currentTimeMillis()
            startLocationUpdates()
            TrackingState.Tracking(
                speed = "0.0",
                distance = "0.0",
                time = "00:00",
                geoPoints = emptyList(),
                isPaused = false
            )
        } else {
            TrackingState.Error("Location permission required")
        }
    }

    private fun startLocationUpdates() {
        trackingJob?.cancel()
        trackingJob = viewModelScope.launch {
            repository.getLocationUpdates()
                .catch { e ->
                    _state.value =  TrackingState.Error(e.message ?: "Location tracking error")
                }
                .collect { (geoPoint, speed) ->
                    processEvent(TrackingEvent.LocationUpdate(geoPoint, speed))
                }
        }
    }

    private fun handleTogglePause() {
        val currentState = (_state as? TrackingState.Tracking) ?: return
        val isPaused = !currentState.isPaused

        if (isPaused) {
            pauseOffset = System.currentTimeMillis() - startTime
            trackingJob?.cancel()
        } else {
            startTime = System.currentTimeMillis() - pauseOffset
            startLocationUpdates()
        }

        _state.value =  currentState.copy(
            isPaused = isPaused,
            time = calculateElapsedTime(currentState)
        )
    }

    private fun handleStopTracking() {
        trackingJob?.cancel()
        _state.value =  TrackingState.Idle
        resetTrackingData()
    }

    private fun handleAbandonTracking() {
        trackingJob?.cancel()
        _state.value =  TrackingState.Idle
        resetTrackingData()
    }

    private fun handleLocationUpdate(geoPoint: GeoPoint, speed: Float) {
        viewModelScope.launch {
            val currentState = (_state as? TrackingState.Tracking) ?: return@launch
            val newPoints = currentState.geoPoints + geoPoint

            val distance = repository.calculateDistance(newPoints)
            val formattedDistance = "%.2f".format(distance / 1000)

            _state.value =  currentState.copy(
                speed = "%.1f".format(speed * 3.6),
                distance = formattedDistance,
                geoPoints = newPoints,
                time = calculateElapsedTime(currentState)
            )
        }
    }

    private fun calculateElapsedTime(state: TrackingState.Tracking): String {
        val elapsedMillis = if (state.isPaused) pauseOffset
        else System.currentTimeMillis() - startTime

        val minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedMillis)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedMillis) % 60

        return "%02d:%02d".format(minutes, seconds)
    }

    private fun resetTrackingData() {
        startTime = 0L
        pauseOffset = 0L
    }

    override fun onCleared() {
        super.onCleared()
        trackingJob?.cancel()
    }
}
