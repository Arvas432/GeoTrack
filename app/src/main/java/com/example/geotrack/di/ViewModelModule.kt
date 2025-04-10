package com.example.geotrack.di

import com.example.geotrack.ui.tracking.viewmodel.TrackingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { TrackingViewModel(get()) }
}