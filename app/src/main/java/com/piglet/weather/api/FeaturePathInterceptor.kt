package com.piglet.weather.api

import com.piglet.weather.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class FeaturePathInterceptor : Interceptor {
    companion object {
        private const val APP_ID = "appid"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val appId = "$APP_ID=" + BuildConfig.API_KEY
        val fullUrl = "${chain.request().url}&${appId}"

        val customRequest = chain.request().newBuilder()
            .url(fullUrl)
            .build()

        return chain.proceed(customRequest)
    }
}
