package com.example.geotrack.ui.social.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.geotrack.domain.public_tracks.Post
import com.example.geotrack.domain.social.PostsInteractor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RoutesViewModel(
    private val interactor: PostsInteractor
) : ViewModel() {

    private val _state = MutableStateFlow<RoutesState>(RoutesState.Loading)
    val state: StateFlow<RoutesState> = _state.asStateFlow()
    private val posts: MutableList<Post> = mutableListOf()

    init {
        processIntent(RoutesIntent.LoadPosts)
    }

    fun processIntent(intent: RoutesIntent) {
        when (intent) {
            is RoutesIntent.LoadPosts -> loadPosts()
            is RoutesIntent.RatePost -> likePost(intent.postId)
        }
    }

    private fun loadPosts() {
        viewModelScope.launch {
            _state.value = RoutesState.Loading
            val result = interactor.loadPosts()
            when {
                result.isNotEmpty() -> {
                    Log.i("result", result.toString())
                    _state.value = RoutesState.Loaded(result)
                    result.map { post ->  if (posts.find { it.serverId == post.serverId} == null) posts.add(post) }
                }

                else -> _state.value = RoutesState.Error("Ошибка загрузки ленты")
            }
        }
    }

    private fun likePost(postId: Long) {
        viewModelScope.launch {
            val post = posts.find { it.serverId == postId }
            if (post != null) {
                if (!post.liked){
                    interactor.like(postId)
                } else {
                    interactor.unlike(postId)
                }
                posts.map { if (it.serverId == postId) it.copy(liked = !it.liked) else it }
            }
            loadPosts()
        }
    }

}

