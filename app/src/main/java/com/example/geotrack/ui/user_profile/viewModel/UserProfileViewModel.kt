package com.example.geotrack.ui.user_profile.viewModel

import android.content.ContentResolver
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geotrack.domain.profile.UserProfileInteractor
import com.example.geotrack.domain.routeTracking.TrackRepository
import com.example.geotrack.ui.user_profile.state.UserProfileState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val trackRepository: TrackRepository,
    private val userProfileInteractor: UserProfileInteractor,
) : ViewModel() {
    private val _state = MutableStateFlow(UserProfileState())
    val state: StateFlow<UserProfileState> = _state.asStateFlow()

    init {
        loadTracks()
        loadProfileData()
    }

    private fun loadProfileData() {
        viewModelScope.launch {
            userProfileInteractor.getProfile()?.let { profile ->
                _state.update {
                    it.copy(
                        name = profile.name,
                        profileImageBitmap = profile.profileImageBitmap,
                        completedRoutes = profile.completedRoutes
                    )
                }
            }
        }
    }


    private fun loadTracks() {
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