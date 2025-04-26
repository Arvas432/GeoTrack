package com.example.geotrack.di

import android.content.ContentResolver
import androidx.room.Room
import com.example.geotrack.data.db.AppDatabase
import com.example.geotrack.data.db.TrackDao
import com.example.geotrack.data.local.LocalImageStorageHandler
import com.example.geotrack.data.local.LocalImageStorageHandlerImpl
import com.example.geotrack.data.location.TrackRepositoryImpl
import com.example.geotrack.domain.routeTracking.TrackInteractor
import com.example.geotrack.domain.routeTracking.TrackRepository
import com.example.geotrack.domain.routeTracking.impl.TrackInteractorImpl
import com.example.geotrack.util.GpxConverter
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val storageModule = module {
    factory<TrackInteractor> { TrackInteractorImpl(get(), get()) }
    single { Room.databaseBuilder(
        get(),
        AppDatabase::class.java, "tracks-db"
    ).build() }
    single<TrackDao> { get<AppDatabase>().trackDao() }
    single<TrackRepository> { TrackRepositoryImpl(get(), get()) }
    single<ContentResolver> { androidContext().contentResolver }
    single<GpxConverter> { GpxConverter() }
    single<LocalImageStorageHandler> {LocalImageStorageHandlerImpl(get(), androidContext())}
}