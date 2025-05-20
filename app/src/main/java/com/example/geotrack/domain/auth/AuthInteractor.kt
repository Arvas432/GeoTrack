package com.example.geotrack.domain.auth

import com.example.geotrack.data.network.dto.UserCredentials
import com.example.geotrack.domain.auth.model.ResultType

interface AuthInteractor {
    suspend fun login(userCredentials: UserCredentials): Result<ResultType>
    suspend fun register(userCredentials: UserCredentials): Result<ResultType>
    fun getToken(): String?
    fun clearToken()
    suspend fun checkToken(): Boolean
}