package com.example.geotrack.data.profile

import com.example.geotrack.data.db.UserProfileDao
import com.example.geotrack.domain.profile.UserProfileRepository
import com.example.geotrack.domain.profile.model.UserProfile
import com.example.geotrack.util.UserProfileMapper

class UserProfileRepositoryImpl(private val dao: UserProfileDao): UserProfileRepository {
    override suspend fun saveProfile(profile: UserProfile) {
        dao.saveProfile(UserProfileMapper.toEntity(profile))
    }

    override suspend fun getProfile(): UserProfile? = UserProfileMapper.fromEntity(dao.getProfile())

    override suspend fun updateProfile(profile: UserProfile) {
        dao.updateProfile(UserProfileMapper.toEntity(profile))
    }

}