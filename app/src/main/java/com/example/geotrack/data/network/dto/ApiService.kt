package com.example.geotrack.data.network.dto
import retrofit2.http.*

interface ApiService {
    @POST("register")
    suspend fun register(@Body credentials: UserCredentials)

    @POST("login")
    suspend fun login(@Body credentials: UserCredentials): AuthResponse

    @GET("tracks")
    suspend fun getTracks(@Header("Authorization") token: String): List<TrackDto>

    @POST("tracks")
    suspend fun uploadTrack(
        @Header("Authorization") token: String,
        @Body track: TrackDto
    )
}