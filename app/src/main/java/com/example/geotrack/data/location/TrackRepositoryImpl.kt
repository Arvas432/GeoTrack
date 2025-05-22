package com.example.geotrack.data.location

import android.graphics.Bitmap
import android.util.Log
import com.example.geotrack.data.db.TrackDao
import com.example.geotrack.data.local.LocalImageStorageHandler
import com.example.geotrack.data.network.NetworkClient
import com.example.geotrack.data.network.RetrofitNetworkClient
import com.example.geotrack.data.network.dto.TracksRequest
import com.example.geotrack.data.network.dto.TracksResponse
import com.example.geotrack.data.network.dto.UploadTrackRequest
import com.example.geotrack.domain.auth.TokenStorage
import com.example.geotrack.domain.routeTracking.TrackRepository
import com.example.geotrack.domain.routeTracking.model.Track
import com.example.geotrack.util.TrackMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class TrackRepositoryImpl(
    private val trackDao: TrackDao,
    private val storageHandler: LocalImageStorageHandler,
    private val networkClient: NetworkClient,
    private val tokenStorage: TokenStorage
) : TrackRepository {
    override suspend fun saveTrack(track: Track) {
        var imagePath: String? = null
        if (track.image != null) {
            imagePath =
                storageHandler.createImageFile(track.image, track.name + System.currentTimeMillis())
        }
        trackDao.insert(TrackMapper.mapModelToEntity(track, imagePath))
        Log.i("ID",track.localDbId.toString())
        val savedTrackEntity = trackDao.getTrackById(track.localDbId)
        Log.i("SAVED TRACK ENTITY", savedTrackEntity.toString())

        val savedTrackModel = savedTrackEntity?.let { TrackMapper.mapEntityToModel(it, track.image) }
        Log.i("SAVED TRACK MODEL", savedTrackModel.toString())
        val token = tokenStorage.getToken()
        Log.i("TOKEN", token.toString())
        if (token != null) {
            savedTrackModel?.let { UploadTrackRequest(token, it) }
                ?.let { networkClient.doRequest(it) }
        }

    }

    override fun getAllTracks(): Flow<List<Track>> = flow {
        val localTracks = trackDao.getAllTracks().map {
            var image: Bitmap? = null
            if (it.imageFilePath != null) {
                image = storageHandler.readBitmapFromFilePath(it.imageFilePath)
            }
            TrackMapper.mapEntityToModel(it, image)
        }
        val token = tokenStorage.getToken()
        if (token!=null) {
            val response = networkClient.doRequest(TracksRequest(token))
            if (response.resultCode == RetrofitNetworkClient.SUCCESS && response is TracksResponse) {
                val remoteTracks = response.tracks
                val localTrackIds = localTracks.map { it.localDbId }.toSet()
                val remoteTrackIds = remoteTracks.map { it.localDbId }.toSet()
                val missingInLocal = remoteTracks.filterNot { it.localDbId in localTrackIds }

                val missingInRemote = localTracks.filterNot { it.localDbId in remoteTrackIds }
                missingInLocal.forEach { track ->
                    var imagePath: String? = null
                    if (track.image != null) {
                        imagePath =
                            storageHandler.createImageFile(track.image, track.name + System.currentTimeMillis())
                    }
                    trackDao.insert(TrackMapper.mapModelToEntity(track, imagePath))
                }

                missingInRemote.forEach { track ->
                    networkClient.doRequest(UploadTrackRequest(token, track))
                }
                Log.i("OUTPUT TRACKS", (localTracks + missingInLocal).toString())
                emit(localTracks + missingInLocal)
            }
        } else {
            Log.i("OUTPUT TRACKS", localTracks.toString())
            emit(localTracks)
        }
    }

    override suspend fun deleteTrack(trackId: Long) {
        trackDao.delete(trackId)
    }

    override suspend fun getTrackById(id: Long): Track? = withContext(Dispatchers.IO) {
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