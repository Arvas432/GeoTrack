package com.example.geotrack.data.auth

import com.example.geotrack.data.network.NetworkClient
import com.example.geotrack.data.network.RegisterRequest
import com.example.geotrack.data.network.RetrofitNetworkClient
import com.example.geotrack.data.network.dto.AuthResponse
import com.example.geotrack.data.network.dto.LoginRequest
import com.example.geotrack.data.network.dto.TokenCheckRequest
import com.example.geotrack.data.network.dto.UserCredentials
import com.example.geotrack.domain.auth.AuthRepository
import com.example.geotrack.domain.auth.TokenStorage
import com.example.geotrack.domain.auth.model.AuthResource
import com.example.geotrack.domain.auth.model.ResultType

class AuthRepositoryImpl(
    private val networkClient: NetworkClient,
    private val tokenStorage: TokenStorage
) : AuthRepository {
    override suspend fun login(userCredentials: UserCredentials): AuthResource {
        val response = networkClient.doRequest(
            LoginRequest(
                userCredentials.username,
                userCredentials.password
            )
        )
        return if (response is AuthResponse) {
            AuthResource(token = response.token, resultType = ResultType.SUCCESS)
        } else {
            when (response.resultCode) {
                RetrofitNetworkClient.NO_CONNECTION -> AuthResource(resultType = ResultType.NO_CONNECTION)
                else -> {
                    AuthResource(resultType = ResultType.SERVER_ERROR)
                }
            }
        }
    }

    override suspend fun register(userCredentials: UserCredentials): AuthResource {
        val response = networkClient.doRequest(
            RegisterRequest(
                userCredentials.username,
                userCredentials.password
            )
        )
        return when (response.resultCode) {
            RetrofitNetworkClient.SUCCESS -> AuthResource(resultType = ResultType.SUCCESS)
            RetrofitNetworkClient.NO_CONNECTION -> AuthResource(resultType = ResultType.NO_CONNECTION)
            else -> {
                AuthResource(resultType = ResultType.SERVER_ERROR)
            }
        }
    }

    override suspend fun checkToken(): AuthResource {
        val response = tokenStorage.getToken()
            ?.let { TokenCheckRequest(it) }?.let { networkClient.doRequest(it) }
        return if (response != null) {
            AuthResource(resultType = if (response.resultCode == RetrofitNetworkClient.SUCCESS) ResultType.SUCCESS else ResultType.SERVER_ERROR)
        } else AuthResource(resultType = ResultType.SERVER_ERROR)
    }

}