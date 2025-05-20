package com.example.geotrack.domain.profile

import com.example.geotrack.domain.profile.model.UserProfile

interface UserProfileRepository {
    suspend fun saveProfile(profile: UserProfile)
    suspend fun getProfile(): UserProfile?
    suspend fun updateProfile(profile: UserProfile)
}