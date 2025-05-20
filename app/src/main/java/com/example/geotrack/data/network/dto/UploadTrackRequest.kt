package com.example.geotrack.data.network.dto

import com.example.geotrack.domain.routeTracking.model.Track

data class UploadTrackRequest(val token: String, val track: Track)
