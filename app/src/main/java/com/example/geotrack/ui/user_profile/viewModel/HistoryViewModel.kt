package com.example.geotrack.ui.user_profile.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geotrack.domain.routeTracking.TrackRepository
import com.example.geotrack.ui.user_profile.state.HistoryState
import com.example.geotrack.util.TrackMapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val trackRepository: TrackRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(HistoryState())
    val state: StateFlow<HistoryState> = _state.asStateFlow()

    init {
        loadTracks()
    }

    fun loadTracks() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                trackRepository.getAllTracks()
                    .collect { tracks ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                tracks = tracks.sortedByDescending { item -> item.date }
                            )
                        }
                    }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Не удалось загрузить маршруты"
                    )
                }
            }
        }
    }

    fun deleteTrack(trackId: Long) {
        viewModelScope.launch {
            try {
                trackRepository.deleteTrack(trackId)
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = "Не удалось удалить маршрут: ${e.localizedMessage}")
                }
            }
        }
    }
}