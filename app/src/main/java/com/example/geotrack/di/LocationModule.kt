package com.example.geotrack.di

import com.example.geotrack.data.GeoRepositoryImpl
import com.example.geotrack.domain.routeTracking.GeoRepository
import com.example.geotrack.ui.tracking.viewmodel.TrackingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val locationModule = module {
    single<GeoRepository> { GeoRepositoryImpl(androidContext()) }
    viewModel { TrackingViewModel(get()) }
}