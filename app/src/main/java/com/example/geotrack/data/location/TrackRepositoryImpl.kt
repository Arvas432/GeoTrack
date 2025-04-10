package com.example.geotrack.data.location

import com.example.geotrack.data.db.TrackDao
import com.example.geotrack.domain.routeTracking.TrackRepository
import com.example.geotrack.domain.routeTracking.model.Track
import com.example.geotrack.util.TrackMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class TrackRepositoryImpl(
    private val trackDao: TrackDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TrackRepository {
    override suspend fun saveTrack(track: Track) {
        trackDao.insert(TrackMapper.mapModelToEntity(track))
    }

    override fun getAllTracks(): Flow<List<Track>> = flow {
        val tracks = trackDao.getAllTracks().map {
            TrackMapper.mapEntityToModel(it)
        }
        emit(tracks)
    }

    override suspend fun getTrackById(id: Long): Track? = withContext(dispatcher) {
        val result = trackDao.getTrackById(id)
        if (result != null) {
            TrackMapper.mapEntityToModel(result)
        } else {
            null
        }
    }

    override suspend fun deleteTrack(track: Track) = withContext(dispatcher) {
        trackDao.delete(TrackMapper.mapModelToEntity(track))
    }
}