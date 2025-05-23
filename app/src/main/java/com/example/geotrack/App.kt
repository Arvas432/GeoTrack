package com.example.geotrack

import android.app.Application
import com.example.geotrack.di.authModule
import com.example.geotrack.di.locationModule
import com.example.geotrack.di.networkModule
import com.example.geotrack.di.profileModule
import com.example.geotrack.di.storageModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.osmdroid.config.Configuration
import java.io.File



class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(locationModule, storageModule, profileModule, authModule, networkModule)
        }
        val osmConf = Configuration.getInstance()
        val basePath = File(cacheDir.absolutePath, "osmdroid")
        osmConf.osmdroidBasePath = basePath
        val tileCache = File(osmConf.osmdroidBasePath.absolutePath, "tile")
        osmConf.osmdroidTileCache = tileCache
    }
}
