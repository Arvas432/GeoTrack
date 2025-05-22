package com.example.geotrack.data.network.dto
import okhttp3.ResponseBody
import retrofit2.Response
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
        @Body track: UploadTrackDto
    )
    @DELETE("tracks/{id}")
    suspend fun deleteTrack(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    )

    @GET("public-tracks")
    suspend fun getPublicTracks(): List<PostDto>

    @POST("public-tracks/{id}/like")
    suspend fun likeTrack(@Path("id") id: Long): ResponseBody

    @POST("public-tracks/{id}/unlike")
    suspend fun unlikeTrack(@Path("id") id: Long): ResponseBody

    @POST("make-public/{id}")
    suspend fun makeTrackPublic(@Path("id") id: Long): ResponseBody

    @GET("tokencheck")
    suspend fun checkToken(
        @Header("Authorization") token: String
    ): Response<Unit>
}