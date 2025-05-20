package com.example.geotrack.domain.auth

import com.example.geotrack.data.network.dto.UserCredentials
import com.example.geotrack.domain.auth.model.AuthResource

interface AuthRepository {
    suspend fun login(userCredentials: UserCredentials): AuthResource
    suspend fun register(userCredentials: UserCredentials): AuthResource
}