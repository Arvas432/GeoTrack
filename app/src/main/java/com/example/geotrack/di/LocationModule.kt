package com.example.geotrack.di

import androidx.room.Room
import com.example.geotrack.data.db.AppDatabase
import com.example.geotrack.data.db.TrackDao
import com.example.geotrack.data.location.GeoRepositoryImpl
import com.example.geotrack.data.location.TrackRepositoryImpl
import com.example.geotrack.domain.routeTracking.GeoRepository
import com.example.geotrack.domain.routeTracking.TrackInteractor
import com.example.geotrack.domain.routeTracking.TrackRepository
import com.example.geotrack.domain.routeTracking.impl.TrackInteractorImpl
import com.example.geotrack.ui.tracking.viewmodel.TrackingViewModel
import com.example.geotrack.util.GpxConverter
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val locationModule = module {
    single<GeoRepository> { GeoRepositoryImpl(androidContext()) }
    viewModel { TrackingViewModel(get(), get(), get(), get()) }

}