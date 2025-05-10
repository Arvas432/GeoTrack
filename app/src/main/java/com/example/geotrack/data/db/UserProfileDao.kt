package com.example.geotrack.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.geotrack.domain.profile.model.UserProfile

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(profile: UserProfileEntity)

    @Query("SELECT * FROM user_profiles LIMIT 1")
    suspend fun getProfile(): UserProfileEntity?

    @Update
    suspend fun updateProfile(profile: UserProfileEntity)
}