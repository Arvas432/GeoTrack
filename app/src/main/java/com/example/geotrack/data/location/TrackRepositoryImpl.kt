package com.example.geotrack.data.location

import android.graphics.Bitmap
import android.util.Log
import com.example.geotrack.data.db.TrackDao
import com.example.geotrack.data.local.LocalImageStorageHandler
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
    private val storageHandler: LocalImageStorageHandler,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : TrackRepository {
    override suspend fun saveTrack(track: Track) {
        var imagePath: String? = null
        if (track.image != null) {
            imagePath =
                storageHandler.createImageFile(track.image, track.name + System.currentTimeMillis())
            Log.i("КАРТИНКА ПРИ СОХРАНЕНИИ", imagePath.toString())
        }
        trackDao.insert(TrackMapper.mapModelToEntity(track, imagePath))
    }

    override fun getAllTracks(): Flow<List<Track>> = flow {
        val tracks = trackDao.getAllTracks().map {
            var image: Bitmap? = null
            if (it.imageFilePath != null) {
                image = storageHandler.readBitmapFromFilePath(it.imageFilePath)
            }
            TrackMapper.mapEntityToModel(it, image)
        }
        emit(tracks)
    }

    override suspend fun deleteTrack(trackId: Long) {
        trackDao.delete(trackId)
    }

    override suspend fun getTrackById(id: Long): Track? = withContext(dispatcher) {
        val result = trackDao.getTrackById(id)
        if (result != null) {
            var image: Bitmap? = null
            if (result.imageFilePath != null) {
                image = storageHandler.readBitmapFromFilePath(result.imageFilePath)
            }
            TrackMapper.mapEntityToModel(result, image)
        } else {
            null
        }
    }
}