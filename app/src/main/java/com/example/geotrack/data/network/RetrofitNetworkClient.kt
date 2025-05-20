package com.example.geotrack.data.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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

class RetrofitNetworkClient(private val apiService: ApiService, private val connectivityManager: ConnectivityManager) : NetworkClient {
    override suspend fun doRequest(dto: Any): Response {
        if (!isConnected()) return Response(NO_CONNECTION)
        val response = when (dto) {
            is RegisterRequest -> handleRegisterRequest(dto)
            is LoginRequest -> handleLoginRequest(dto)
            is TracksRequest -> handleTracksRequest(dto)
            is UploadTrackRequest -> handleTrackUploadRequest(dto)
            else -> {
                Response(INTERNAL_ERROR)
            }
        }
        return response
    }

    private suspend fun handleRegisterRequest(request: RegisterRequest): Response {
        return withContext(Dispatchers.IO) {
            try {
                apiService.register(UserCredentials(request.login, request.password))
                Response(SUCCESS)
            } catch (e: Throwable) {
                Response(ERROR)
            }
        }
    }

    private suspend fun handleLoginRequest(request: LoginRequest): Response {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.login(UserCredentials(request.login, request.password))
                response.apply { resultCode = SUCCESS }
            } catch (e: Throwable) {
                Response(ERROR)
            }
        }
    }

    private suspend fun handleTracksRequest(request: TracksRequest): Response {
        return withContext(Dispatchers.IO) {
            try {
                val tracks = apiService.getTracks(request.token)
                TracksResponse(tracks.map { TrackMapper.mapDtoToModel(it) })
            } catch (e: Throwable) {
                Response(ERROR)
            }
        }
    }
    private suspend fun handleTrackUploadRequest(request: UploadTrackRequest): Response {
        return withContext(Dispatchers.IO) {
            try {
                apiService.uploadTrack(request.token, TrackMapper.mapModelToDto(request.track, "ЗАГЛУШКА ФУЛ ПОНОС!!!"))
                Response(SUCCESS)
            } catch (e: Throwable) {
                Response(ERROR)
            }
        }
    }
    private fun isConnected(): Boolean {
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.run {
            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } ?: false
    }
    companion object {
        const val SUCCESS = 200
        const val ERROR = 400
        const val INTERNAL_ERROR = 500
        const val NO_CONNECTION = -1
    }
}