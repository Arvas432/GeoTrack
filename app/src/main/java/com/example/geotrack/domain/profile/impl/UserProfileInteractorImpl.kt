package com.example.geotrack.domain.profile.impl

import com.example.geotrack.domain.profile.UserProfileInteractor
import com.example.geotrack.domain.profile.UserProfileRepository
import com.example.geotrack.domain.profile.model.UserProfile

class UserProfileInteractorImpl(private val repository: UserProfileRepository): UserProfileInteractor {
    override suspend fun saveProfile(profile: UserProfile) {
        repository.saveProfile(profile)
    }

    override suspend fun getProfile(): UserProfile? {
        return repository.getProfile()
    }

    override suspend fun incrementRoutesCompleted() {
        repository.getProfile()?.let { profile ->
            repository.saveProfile(profile.copy(
                completedRoutes = profile.completedRoutes + 1
            ))
        }
    }

}