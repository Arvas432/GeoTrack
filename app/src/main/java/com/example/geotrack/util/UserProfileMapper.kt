package com.example.geotrack.util

import android.graphics.Bitmap
import com.example.geotrack.data.db.UserProfileEntity
import com.example.geotrack.domain.profile.model.UserProfile

object UserProfileMapper {
    fun toEntity(domain: UserProfile, imageFilePath: String?) = UserProfileEntity(
        id = domain.id,
        name = domain.name,
        height = domain.height,
        weight = domain.weight,
        completedRoutes = domain.completedRoutes,
        profileImageFilepath = imageFilePath
    )

    fun fromEntity(entity: UserProfileEntity?, imageBitmap: Bitmap?) = entity?.let {
        UserProfile(
            id = it.id,
            name = it.name,
            height = it.height,
            weight = it.weight,
            completedRoutes = it.completedRoutes,
            profileImageBitmap = imageBitmap
        )
    }
}