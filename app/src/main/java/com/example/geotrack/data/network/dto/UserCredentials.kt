package com.example.geotrack.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserCredentials(
    val username: String,
    val password: String
)
