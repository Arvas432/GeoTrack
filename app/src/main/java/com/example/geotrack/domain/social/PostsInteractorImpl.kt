package com.example.geotrack.domain.social

import com.example.geotrack.domain.public_tracks.Post

class PostsInteractorImpl(private val repository: PostsRepository): PostsInteractor {
    override suspend fun loadPosts(): List<Post> = repository.getPublicPosts()
    override suspend fun like(postId: Long): Boolean = repository.likePost(postId)
    override suspend fun unlike(postId: Long): Boolean = repository.unlikePost(postId)
}
