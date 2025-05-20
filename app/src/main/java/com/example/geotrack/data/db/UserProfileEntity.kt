package com.example.geotrack.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val height: Int,
    val weight: Int,
    val completedRoutes: Int = 0,
    val profileImageFilepath: String? = null
)
