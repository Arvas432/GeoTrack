package com.example.geotrack.di

import com.example.geotrack.data.FusedLocationRepository
import com.example.geotrack.domain.tracking.LocationRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<LocationRepository> { FusedLocationRepository(get()) }
}