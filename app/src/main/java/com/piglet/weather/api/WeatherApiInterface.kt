package com.piglet.weather.api

import com.piglet.weather.data.model.response.WeatherLatLongModelResponse
import com.piglet.weather.data.model.response.WeatherModelResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiInterface {

    @GET("geo/1.0/direct")
    suspend fun getLatLong(
        @Query("q") location: String
    ): Response<List<WeatherLatLongModelResponse>>

    @GET("data/2.5/onecall")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("units") units: String
    ): Response<WeatherModelResponse>
}