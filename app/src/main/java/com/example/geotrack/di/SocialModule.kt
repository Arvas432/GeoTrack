package com.example.geotrack.di

import com.example.geotrack.data.social.PostsRepositoryImpl
import com.example.geotrack.domain.social.PostsInteractor
import com.example.geotrack.domain.social.PostsInteractorImpl
import com.example.geotrack.domain.social.PostsRepository
import com.example.geotrack.ui.social.viewmodel.RoutesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val socialModule = module {
    single<PostsRepository> { PostsRepositoryImpl(get()) }
    factory<PostsInteractor> { PostsInteractorImpl(get()) }
    viewModel<RoutesViewModel> {RoutesViewModel(get())}
}