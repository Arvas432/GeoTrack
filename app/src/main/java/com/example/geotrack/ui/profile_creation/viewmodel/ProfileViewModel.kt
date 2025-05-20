package com.example.geotrack.ui.profile_creation.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geotrack.domain.profile.UserProfileInteractor
import com.example.geotrack.ui.profile_creation.state.ProfileCreationState
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val interactor: UserProfileInteractor
) : ViewModel() {
    private val _uiState = mutableStateOf(ProfileCreationState())
    val uiState: State<ProfileCreationState> = _uiState

    fun setProfileImage(bitmap: Bitmap?) {
        _uiState.value = _uiState.value.copy(profileImageBitmap = bitmap)
    }

    fun setName(value: String) {
        _uiState.value = _uiState.value.copy(name = value)
    }

    fun setHeight(value: String) {
        if (value.matches(Regex("^\\d*\$"))) {
            _uiState.value = _uiState.value.copy(height = value)
        }
    }

    fun setWeight(value: String) {
        if (value.matches(Regex("^\\d*\$"))) {
            _uiState.value = _uiState.value.copy(weight = value)
        }
    }

    fun saveProfile() {
        viewModelScope.launch {
            interactor.saveProfile(
                profile = _uiState.value.toDomainModel()
            )
        }
    }

    init {
        loadExistingProfile()
    }

    private fun loadExistingProfile() {
        viewModelScope.launch {
            interactor.getProfile()?.let { profile ->
                _uiState.value = ProfileCreationState(
                    id = profile.id,
                    name = profile.name,
                    height = profile.height.toString(),
                    weight = profile.weight.toString(),
                    completedRoutes = profile.completedRoutes,
                    profileImageBitmap = profile.profileImageBitmap
                )
            }
        }
    }
}