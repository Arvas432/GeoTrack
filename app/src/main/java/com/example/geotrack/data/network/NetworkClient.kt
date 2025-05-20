package com.example.geotrack.data.network

import com.example.geotrack.data.network.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}