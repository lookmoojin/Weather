package com.piglet.weather.navigation.router

import android.os.Bundle
import com.piglet.foundation.extension.navigateSafe
import com.piglet.weather.navigation.Destination
import com.piglet.weather.navigation.Router
import com.piglet.weather.navigation.config.ConfigSearchNavigate

class SearchRouter : Router() {

    override fun routeTo(destination: Destination, bundle: Bundle?) {
        super.routeTo(destination, bundle)
        val navigationId = ConfigSearchNavigate[destination]
        navigationId?.let { _navigationId ->
            navController?.navigateSafe(_navigationId, bundle)
        }
    }
}
