package com.example.geotrack.data.social

import android.util.Log
import com.example.geotrack.data.network.NetworkClient
import com.example.geotrack.data.network.RetrofitNetworkClient
import com.example.geotrack.data.network.dto.LikeTrackRequest
import com.example.geotrack.data.network.dto.PublicTracksRequest
import com.example.geotrack.data.network.dto.PublicTracksResponse
import com.example.geotrack.data.network.dto.UnlikeTrackRequest
import com.example.geotrack.domain.public_tracks.Post
import com.example.geotrack.domain.social.PostsRepository
import com.example.geotrack.util.TrackMapper

class PostsRepositoryImpl(private val networkClient: NetworkClient) : PostsRepository {
    override suspend fun getPublicPosts(): List<Post> {
        val response = networkClient.doRequest(PublicTracksRequest)
        Log.i("Response", response.resultCode.toString())
        return if (response.resultCode == RetrofitNetworkClient.SUCCESS) {
            Log.i("Response success", (response as PublicTracksResponse).tracks.toString())
            (response as PublicTracksResponse).tracks
        } else emptyList()
    }

    override suspend fun likePost(postId: Long): Boolean {
        val response = networkClient.doRequest(LikeTrackRequest(postId))
        return response.resultCode == RetrofitNetworkClient.SUCCESS
    }

    override suspend fun unlikePost(postId: Long): Boolean {
        val response = networkClient.doRequest(UnlikeTrackRequest(postId))
        return response.resultCode == RetrofitNetworkClient.SUCCESS
    }
}
