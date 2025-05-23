package com.example.geotrack.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey(autoGenerate = false) val id: Long,
    val name: String,
    val date: Long,
    val duration: Long,
    val distance: Double,
    val averageSpeed: Double,
    val gpxData: String?,
    val likes: Int?,
    val imageFilePath: String?
)
