package com.example.geotrack.data.network

import com.example.geotrack.data.network.dto.ApiService
import com.example.geotrack.data.network.dto.LoginRequest
import com.example.geotrack.data.network.dto.Response
import com.example.geotrack.data.network.dto.TracksRequest
import com.example.geotrack.data.network.dto.TracksResponse
import com.example.geotrack.data.network.dto.UploadTrackRequest
import com.example.geotrack.data.network.dto.UserCredentials
import com.example.geotrack.util.TrackMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(private val apiService: ApiService) : NetworkClient {
    override suspend fun doRequest(request: Any): Response {
        val response = when (request) {
            is RegisterRequest -> handleRegisterRequest(request)
            is LoginRequest -> handleLoginRequest(request)
            is TracksRequest -> handleTracksRequest(request)
            is UploadTrackRequest -> handleTrackUploadRequest(request)
            else -> {
                Response(500)
            }
        }
        return response
    }

    private suspend fun handleRegisterRequest(request: RegisterRequest): Response {
        return withContext(Dispatchers.IO) {
            try {
                apiService.register(UserCredentials(request.login, request.password))
                Response(200)
            } catch (e: Throwable) {
                Response(400)
            }
        }
    }

    private suspend fun handleLoginRequest(request: LoginRequest): Response {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.login(UserCredentials(request.login, request.password))
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                Response(400)
            }
        }
    }

    private suspend fun handleTracksRequest(request: TracksRequest): Response {
        return withContext(Dispatchers.IO) {
            try {
                val tracks = apiService.getTracks(request.token)
                TracksResponse(tracks.map { TrackMapper.mapDtoToModel(it) })
            } catch (e: Throwable) {
                Response(400)
            }
        }
    }
    private suspend fun handleTrackUploadRequest(request: UploadTrackRequest): Response {
        return withContext(Dispatchers.IO) {
            try {
                apiService.uploadTrack(request.token, TrackMapper.mapModelToDto(request.track, "ЗАГЛУШКА ФУЛ ПОНОС!!!"))
                Response(200)
            } catch (e: Throwable) {
                Response(400)
            }
        }
    }
}