package com.example.geotrack

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.example.geotrack.data.location.GeoRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Rule
import org.junit.Test

class AndroidTests {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @Test
    fun getLocationUpdates_emitsLocation() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repository = GeoRepositoryImpl(context)

        val job = launch {
            withTimeout(10_000) {
                repository.getLocationUpdates().first { location ->
                    location != null
                }
            }
        }

        job.cancelAndJoin()
    }
    @After
    fun clear() {
        Dispatchers.resetMain()
    }
}