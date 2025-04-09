package com.example.geotrack.domain.routeTracking

import com.example.geotrack.domain.routeTracking.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    suspend fun saveTrack(track: Track)
    fun getAllTracks(): Flow<List<Track>>
    suspend fun getTrackById(id: Long): Track?
    suspend fun deleteTrack(track: Track)
}