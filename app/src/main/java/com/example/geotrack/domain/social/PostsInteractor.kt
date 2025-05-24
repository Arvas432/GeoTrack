package com.example.geotrack.domain.social

interface PostsInteractor {
    suspend fun loadPosts(): List<Post>
    suspend fun like(postId: Long): Boolean
    suspend fun unlike(postId: Long): Boolean
}