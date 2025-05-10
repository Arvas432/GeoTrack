package com.example.geotrack.di

import com.example.geotrack.data.profile.UserProfileRepositoryImpl
import com.example.geotrack.domain.profile.UserProfileInteractor
import com.example.geotrack.domain.profile.UserProfileRepository
import com.example.geotrack.domain.profile.impl.UserProfileInteractorImpl
import com.example.geotrack.ui.profile_creation.viewmodel.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val profileModule = module {
    single<UserProfileRepository> {UserProfileRepositoryImpl(get())}
    factory<UserProfileInteractor> { UserProfileInteractorImpl(get()) }
    viewModel<ProfileViewModel> { ProfileViewModel(get()) }
}