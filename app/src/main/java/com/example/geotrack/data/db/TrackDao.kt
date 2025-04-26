package com.example.geotrack.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trackEntity: TrackEntity)

    @Query("SELECT * FROM tracks ORDER BY date DESC")
    suspend fun getAllTracks(): List<TrackEntity>

    @Query("SELECT * FROM tracks WHERE id = :id")
    suspend fun getTrackById(id: Long): TrackEntity?

    @Query("DELETE FROM tracks WHERE id = :trackId")
    suspend fun delete(trackId: Long)
}