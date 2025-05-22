package com.example.geotrack.ui.social.viewmodel

import com.example.geotrack.domain.public_tracks.Post

sealed class RoutesIntent {
    object LoadPosts : RoutesIntent()
    data class RatePost(val postId: Long) : RoutesIntent()
}

sealed class RoutesState {
    object Loading : RoutesState()
    data class Loaded(val posts: List<Post>) : RoutesState()
    data class Error(val message: String) : RoutesState()
}
