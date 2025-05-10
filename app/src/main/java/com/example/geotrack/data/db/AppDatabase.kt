package com.example.geotrack.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TrackEntity::class, UserProfileEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun userProfileDao(): UserProfileDao
}