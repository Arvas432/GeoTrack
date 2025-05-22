package com.example.geotrack.data.network.dto

object PublicTracksRequest
data class LikeTrackRequest(val trackId: Long)
data class UnlikeTrackRequest(val trackId: Long)
data class MakeTrackPublicRequest(val trackId: Long)
