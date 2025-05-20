package com.example.geotrack.di

import com.example.geotrack.data.auth.AuthRepositoryImpl
import com.example.geotrack.data.auth.TokenStorageImpl
import com.example.geotrack.domain.auth.AuthInteractor
import com.example.geotrack.domain.auth.AuthRepository
import com.example.geotrack.domain.auth.TokenStorage
import com.example.geotrack.domain.auth.impl.AuthInteractorImpl
import com.example.geotrack.ui.authorization.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<TokenStorage> { TokenStorageImpl(get()) }
    factory<AuthInteractor> { AuthInteractorImpl(get(), get()) }
    viewModel { AuthViewModel(get()) }
}