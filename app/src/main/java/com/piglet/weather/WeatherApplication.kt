package com.piglet.weather

import android.app.Application
import com.piglet.weather.di.dependenciesModuleList
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@WeatherApplication)
            modules(dependenciesModuleList)
        }
    }
}