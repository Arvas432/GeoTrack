package com.example.geotrack.data.network.dto

import com.example.geotrack.domain.routeTracking.model.Track

data class TracksResponse(val tracks: List<Track>): Response()