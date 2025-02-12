package com.example.geotrack.domain

import android.net.Uri

data class Route(val id: Int, val title: String, val date: String, val avgSpeed: Float, val distance: Float, val time: String, val likes: Int, val imageUri: Uri?)