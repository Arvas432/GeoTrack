package com.example.geotrack.domain.auth

interface TokenStorage {
    fun saveToken(token: String)
    fun getToken(): String?
    fun clearToken()
}