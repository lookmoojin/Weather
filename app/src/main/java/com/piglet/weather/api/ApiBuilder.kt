package com.piglet.weather.api

import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.Protocol
import retrofit2.Converter
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

open class BaseHttpClient {

    fun create(): OkHttpClient {
        return baseOkHttpClient().newBuilder()
            .addInterceptor(FeaturePathInterceptor())
            .build()
    }

    private fun baseOkHttpClient(): OkHttpClient {
        val connectionPool = ConnectionPool(5, 5, TimeUnit.MINUTES)
        return UnsafeOkHttpClient.unsafeOkHttpClient.newBuilder()
            .connectionPool(connectionPool)
            .protocols(listOf(Protocol.HTTP_1_1, Protocol.HTTP_2))
            .readTimeout(30000, TimeUnit.MILLISECONDS)
            .writeTimeout(30000, TimeUnit.MILLISECONDS)
            .connectTimeout(30000, TimeUnit.MILLISECONDS)
            .build()
    }
}

open class ApiGsonBuilder(
    val okHttpClient: OkHttpClient,
    val gsonConverterFactory: Converter.Factory
) {
    inline fun <reified T> build(baseUrl: String): T {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(T::class.java)
    }
}
