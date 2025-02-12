package com.example.geotrack.domain

import android.net.Uri

data class User(val id: Int, val name: String, val routesCompleted: Int, val profileImageUri: Uri?)
