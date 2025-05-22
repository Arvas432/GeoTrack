package com.example.geotrack.domain.social

import com.example.geotrack.domain.public_tracks.Post

interface PostsInteractor {
    suspend fun loadPosts(): List<Post>
    suspend fun like(postId: Long): Boolean
    suspend fun unlike(postId: Long): Boolean
}