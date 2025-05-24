package com.example.geotrack.domain.social

interface PostsRepository {
    suspend fun getPublicPosts(): List<Post>
    suspend fun likePost(postId: Long): Boolean
    suspend fun unlikePost(postId: Long): Boolean
}
