package com.example.geotrack.domain.routeTracking

import com.example.geotrack.data.db.TrackEntity
import com.example.geotrack.domain.routeTracking.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    suspend fun saveTrack(track: Track)
    fun getAllTracks(): Flow<List<Track>>
    suspend fun deleteTrack(trackId: Long)
    suspend fun getTrackById(id: Long): Track?
}