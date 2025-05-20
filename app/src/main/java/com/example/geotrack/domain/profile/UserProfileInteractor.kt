package com.example.geotrack.domain.profile

import com.example.geotrack.domain.profile.model.UserProfile

interface UserProfileInteractor {
    suspend fun saveProfile(profile: UserProfile)
    suspend fun getProfile(): UserProfile?
    suspend fun incrementRoutesCompleted()
}