package com.piglet.weather.extension

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import timber.log.Timber

fun NavController.navigateSafe(_navigationId: Int, bundle: Bundle? = null) {
    try {
        this.navigate(_navigationId, bundle)
    } catch (ex: IllegalArgumentException) {
        Timber.e("Can't open 2 links at once! ${ex.message}")
    } catch (ex: Exception) {
        Timber.e("Can't open 2 links at once! ${ex.message}")
    }
}

fun NavController.navigateSafe(deepLink: Uri, navOptions: NavOptions? = null) {
    try {
        this.navigate(deepLink, navOptions)
    } catch (ex: IllegalArgumentException) {
        Timber.e("Can't open deeplink! ${ex.message}")
    } catch (ex: Exception) {
        Timber.e("Can't open deeplink! ${ex.message}")
    }
}
