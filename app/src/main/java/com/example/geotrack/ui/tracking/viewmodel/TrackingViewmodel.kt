package com.example.geotrack.ui.tracking.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.osmdroid.util.GeoPoint


class TrackingViewModel : ViewModel() {
    // Регистрируемые точки маршрута
    private val _geoPoints = mutableStateListOf<GeoPoint>()
    val geoPoints: List<GeoPoint> get() = _geoPoints

    // Текущая скорость (в км/ч)
    private val _currentSpeed = mutableStateOf("0.0")
    val currentSpeed = mutableStateOf("0.0 км/ч")

    // Общая дистанция (в км)
    private val _totalDistance = mutableStateOf("0.0")
    val totalDistance = mutableStateOf("0 км")

    // Прошедшее время (формат MM:SS)
    private val _elapsedTime = mutableStateOf("0:00")
    val elapsedTime = mutableStateOf("0:00")

    // Состояние паузы
    private val _isPaused = mutableStateOf(false)
    val isPaused: Boolean get() = _isPaused.value

    // Флаг активности трекинга
    private val _isTracking = mutableStateOf(false)
    val isTracking: Boolean get() = _isTracking.value

    // Тайминг
    private var startTime: Long = 0L
    private var pauseOffset: Long = 0L

    fun addLocation(point: GeoPoint, speed: Float) {
        if (!_isPaused.value) {
            _geoPoints.add(point)
            updateSpeed(speed)
            calculateTotalDistance()
            updateElapsedTime()
        }
    }

    fun togglePause() {
        _isPaused.value = !_isPaused.value
        updateTimers()
    }

    fun startTracking() {
        _isTracking.value = true
        startTime = System.currentTimeMillis()
    }

    fun stopTracking() {
        _isTracking.value = false
        resetAll()
    }

    private fun updateSpeed(speed: Float) {
        val kmhSpeed = speed * 3.6f
        _currentSpeed.value = "%.1f".format(kmhSpeed)
        currentSpeed.value = "${_currentSpeed.value} км/ч"
    }

    private fun calculateTotalDistance() {
        var distanceMeters = 0.0
        for (i in 1 until _geoPoints.size) {
            distanceMeters += _geoPoints[i-1].distanceToAsDouble(_geoPoints[i])
        }
        _totalDistance.value = "%.2f".format(distanceMeters / 1000)
        totalDistance.value = "${_totalDistance.value} км"
    }

    private fun updateElapsedTime() {
        val currentTime = if (_isPaused.value) pauseOffset else System.currentTimeMillis() - startTime
        val minutes = (currentTime / 1000) / 60
        val seconds = (currentTime / 1000) % 60
        _elapsedTime.value = "%d:%02d".format(minutes, seconds)
        elapsedTime.value = _elapsedTime.value
    }

    private fun updateTimers() {
        when {
            _isPaused.value -> pauseOffset = System.currentTimeMillis() - startTime
            else -> startTime = System.currentTimeMillis() - pauseOffset
        }
    }

    private fun resetAll() {
        _geoPoints.clear()
        _currentSpeed.value = "0.0"
        _totalDistance.value = "0.0"
        _elapsedTime.value = "0:00"
        currentSpeed.value = "0.0 км/ч"
        totalDistance.value = "0 км"
        elapsedTime.value = "0:00"
        startTime = 0L
        pauseOffset = 0L
        _isPaused.value = false
    }
}