package com.piglet.weather.presentation.search

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.piglet.weather.navigation.Router
import com.piglet.weather.navigation.router.SearchToWeather
import com.piglet.weather.presentation.weather.WeatherFragment.Companion.KEY_EXTRA_LOCATION

class SearchViewModel(
    private val router: Router
) : ViewModel() {

    fun routeToWeather(keyword: String) {
        router.routeTo(
            SearchToWeather,
            Bundle().apply {
                putString(KEY_EXTRA_LOCATION, keyword)
            }
        )
    }
}
