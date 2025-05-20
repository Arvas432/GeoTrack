package com.example.geotrack.di

import android.content.Context
import android.net.ConnectivityManager
import com.example.geotrack.data.network.NetworkClient
import com.example.geotrack.data.network.RetrofitNetworkClient
import com.example.geotrack.data.network.dto.ApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    val BASE_URL = "http://10.0.2.2:8080/"
    single<Gson> {
        GsonBuilder()
            .setStrictness(Strictness.LENIENT)
            .create()
    }

    single {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    single<ConnectivityManager> { androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .client(get())
            .build()
    }
    single<NetworkClient> { RetrofitNetworkClient(get(), get()) }

    single<ApiService> {
        get<Retrofit>().create(ApiService::class.java)
    }

}