package com.example.geotrack.domain.auth.impl

import com.example.geotrack.data.network.NetworkClient
import com.example.geotrack.data.network.dto.UserCredentials
import com.example.geotrack.domain.auth.AuthInteractor
import com.example.geotrack.domain.auth.AuthRepository
import com.example.geotrack.domain.auth.TokenStorage
import com.example.geotrack.domain.auth.model.ResultType

class AuthInteractorImpl(private val authRepository: AuthRepository, private val tokenStorage: TokenStorage): AuthInteractor {
    override suspend fun login(userCredentials: UserCredentials): Result<ResultType> {
        return try {
            val resource = authRepository.login(userCredentials)
            if (resource.token!=null && resource.resultType == ResultType.SUCCESS) {
                tokenStorage.saveToken(resource.token)
                Result.success(ResultType.SUCCESS)
            } else {
               resource.resultType?.let { Result.success(resource.resultType) }?: Result.success(ResultType.SERVER_ERROR)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(userCredentials: UserCredentials): Result<ResultType> {
        return try {
            val resource = authRepository.register(userCredentials)
            resource.resultType?.let { Result.success(resource.resultType) }?: Result.success(ResultType.SERVER_ERROR)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getToken(): String? {
        return tokenStorage.getToken()
    }

    override fun clearToken() {
        tokenStorage.clearToken()
    }

    override suspend fun checkToken(): Boolean {
        return try {
            val resource = authRepository.checkToken()
            resource.resultType == ResultType.SUCCESS
        } catch (e: Exception) {
            false
        }

    }

}