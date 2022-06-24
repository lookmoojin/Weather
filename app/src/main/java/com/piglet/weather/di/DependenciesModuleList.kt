package com.piglet.weather.di

import com.piglet.foundation.di.networkModule

val dependenciesModuleList = listOf(
    networkModule,
    weatherModule
)