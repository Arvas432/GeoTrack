package com.example.geotrack.data.profile

import android.graphics.Bitmap
import com.example.geotrack.data.db.UserProfileDao
import com.example.geotrack.data.local.LocalImageStorageHandler
import com.example.geotrack.domain.User
import com.example.geotrack.domain.profile.UserProfileRepository
import com.example.geotrack.domain.profile.model.UserProfile
import com.example.geotrack.util.UserProfileMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class UserProfileRepositoryImpl(
    private val dao: UserProfileDao,
    private val storageHandler: LocalImageStorageHandler,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserProfileRepository {
    override suspend fun saveProfile(profile: UserProfile) {
        var imagePath: String? = null
        if (profile.profileImageBitmap != null) {
            imagePath =
                storageHandler.createImageFile(profile.profileImageBitmap, "Profile_picture")
        }
        dao.saveProfile(UserProfileMapper.toEntity(profile, imagePath))
    }

    override suspend fun getProfile(): UserProfile? = withContext(dispatcher) {
        val profile = dao.getProfile()
        var image: Bitmap? = null
        if (profile != null) {
            if (profile.profileImageFilepath != null) {
                image = storageHandler.readBitmapFromFilePath(profile.profileImageFilepath)
            }
            UserProfileMapper.fromEntity(profile, image)
        } else {
            null
        }
    }

    override suspend fun updateProfile(profile: UserProfile) {
        var imagePath: String? = null
        if (profile.profileImageBitmap != null) {
            imagePath =
                storageHandler.createImageFile(profile.profileImageBitmap, "Profile_picture")
        }
        dao.updateProfile(UserProfileMapper.toEntity(profile, imagePath))
    }

}