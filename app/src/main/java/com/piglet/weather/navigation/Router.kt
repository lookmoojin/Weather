package com.piglet.weather.navigation

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions

abstract class Router {
    var crossRouter: CrossRouter? = null
    var navController: NavController? = null
    var latestRoute: Destination? = null

    open fun routeTo(destination: Destination, bundle: Bundle? = null) {
        latestRoute = destination
    }

    fun routeWithDeeplink(stringUrl: String, navOptions: NavOptions? = null) {
        crossRouter?.routeWithDeeplink(stringUrl, navOptions)
    }
}
