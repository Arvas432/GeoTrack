package com.example.geotrack.domain.social

import com.example.geotrack.domain.public_tracks.Post

interface PostsRepository {
    suspend fun getPublicPosts(): List<Post>
    suspend fun likePost(postId: Long): Boolean
    suspend fun unlikePost(postId: Long): Boolean
}
