package com.example.geotrack.data.network.dto

import com.example.geotrack.domain.public_tracks.Post

data class PublicTracksResponse(val tracks: List<Post>): Response()
