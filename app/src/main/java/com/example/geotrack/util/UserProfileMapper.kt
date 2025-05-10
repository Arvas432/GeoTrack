package com.example.geotrack.util

import com.example.geotrack.data.db.UserProfileEntity
import com.example.geotrack.domain.profile.model.UserProfile

object UserProfileMapper {
    fun toEntity(domain: UserProfile) = UserProfileEntity(
        id = domain.id,
        name = domain.name,
        height = domain.height,
        weight = domain.weight,
        completedRoutes = domain.completedRoutes,
        profileImageUri = domain.profileImageUri
    )

    fun fromEntity(entity: UserProfileEntity?) = entity?.let {
        UserProfile(
            id = it.id,
            name = it.name,
            height = it.height,
            weight = it.weight,
            completedRoutes = it.completedRoutes,
            profileImageUri = it.profileImageUri
        )
    }
}