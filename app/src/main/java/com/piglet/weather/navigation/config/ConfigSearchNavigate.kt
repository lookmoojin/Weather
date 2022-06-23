package com.piglet.weather.navigation.config

import androidx.collection.arrayMapOf
import com.piglet.weather.R
import com.piglet.weather.navigation.Destination
import com.piglet.weather.navigation.router.SearchToWeather

object ConfigSearchNavigate : Map<Destination, Int> by arrayMapOf(
    SearchToWeather to R.id.action_searchFragment_to_weatherFragment
)
