package com.piglet.weather.navigation

import android.app.Activity
import androidx.navigation.NavOptions

interface CrossRouter {
    var activity: Activity?
    fun routeWithDeeplink(stringUrl: String, navOptions: NavOptions? = null)
}
