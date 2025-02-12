package com.example.geotrack

import android.app.Application
import org.osmdroid.config.Configuration
import java.io.File


class App: Application() {
    override fun onCreate() {
        super.onCreate()
        val osmConf = Configuration.getInstance()
        val basePath = File(cacheDir.absolutePath, "osmdroid")
        osmConf.osmdroidBasePath = basePath
        val tileCache = File(osmConf.osmdroidBasePath.absolutePath, "tile")
        osmConf.osmdroidTileCache = tileCache
    }
}