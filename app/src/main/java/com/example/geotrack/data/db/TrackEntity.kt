package com.example.geotrack.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val date: Long,
    val duration: Long,
    val distance: Double,
    val averageSpeed: Double,
    val gpxData: String
)
