package com.example.geotrack.util

import android.graphics.Bitmap
import com.example.geotrack.data.db.TrackEntity
import com.example.geotrack.data.network.dto.TrackDto
import com.example.geotrack.domain.routeTracking.model.Track
import java.time.Instant
import kotlin.time.DurationUnit
import kotlin.time.toDuration

object TrackMapper {
    fun mapModelToEntity(model: Track, imageFilePath: String?): TrackEntity {
        return TrackEntity(
            id = model.id?:System.currentTimeMillis(),
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

    fun mapDtoToModel(dto: TrackDto): Track {
        return Track(
            id = dto.localId,
            name = dto.name,
            date = Instant.parse(dto.date),
            duration = dto.duration.toDuration(DurationUnit.MILLISECONDS),
            distance = dto.distance,
            averageSpeed = dto.averageSpeed,
            gpxData = dto.gpxData,
            likes = 0, //ХАРДКОД ЗАГЛУШКА САТАНА СМЕРТЬ ФУЛ ПОНОС
            image = dto.imageBase64?.let { ImageSerializer.base64ToBitmap(it) }
        )
    }

    fun mapModelToDto(model: Track, username: String): TrackDto {
        return TrackDto(
            localId = model.id,
            name = model.name,
            date = model.date.toString(),
            duration = model.duration.inWholeMilliseconds,
            distance = model.distance,
            averageSpeed = model.averageSpeed,
            gpxData = model.gpxData,
            imageBase64 = model.image?.let { ImageSerializer.bitmapToBase64(it) },
        )
    }
}