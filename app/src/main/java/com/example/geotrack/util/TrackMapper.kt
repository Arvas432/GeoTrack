package com.example.geotrack.util

import android.graphics.Bitmap
import com.example.geotrack.data.db.TrackEntity
import com.example.geotrack.domain.routeTracking.model.Track
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.time.DurationUnit
import kotlin.time.toDuration

object TrackMapper {
    fun mapModelToEntity(model: Track, imageFilePath: String?): TrackEntity {
        return TrackEntity(
            name = model.name,
            date = model.date.toEpochMilli(),
            duration = model.duration.inWholeMilliseconds,
            distance = model.distance,
            averageSpeed = model.averageSpeed,
            gpxData = model.gpxData,
            likes = model.likes,
            imageFilePath = imageFilePath
        )
    }

    fun mapEntityToModel(entity: TrackEntity, imageBitmap: Bitmap?): Track {
        return Track(
            id = entity.id,
            name = entity.name,
            date = Instant.ofEpochMilli(entity.date),
            duration = entity.duration.toDuration(DurationUnit.MILLISECONDS),
            distance = entity.distance,
            averageSpeed = entity.averageSpeed,
            gpxData = entity.gpxData,
            likes = entity.likes,
            image = imageBitmap
        )
    }
}