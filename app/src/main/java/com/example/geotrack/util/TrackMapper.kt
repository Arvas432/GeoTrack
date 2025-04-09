package com.example.geotrack.util

import com.example.geotrack.data.db.TrackEntity
import com.example.geotrack.domain.routeTracking.model.Track
import java.time.Instant
import kotlin.time.DurationUnit
import kotlin.time.toDuration

object TrackMapper {
    fun mapModelToEntity(model: Track): TrackEntity {
        return TrackEntity(
            name = model.name,
            date = model.date.toEpochMilli(),
            duration = model.duration.inWholeMilliseconds,
            distance = model.distance,
            averageSpeed = model.averageSpeed,
            gpxData = model.gpxData ?: ""
        )
    }

    fun mapEntityToModel(entity: TrackEntity): Track {
        return Track(
            id = entity.id,
            name = entity.name,
            date = Instant.ofEpochMilli(entity.date),
            duration = entity.duration.toDuration(DurationUnit.MINUTES),
            distance = entity.distance,
            averageSpeed = entity.averageSpeed,
            gpxData = entity.gpxData
        )
    }
}