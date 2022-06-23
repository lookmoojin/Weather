package com.piglet.weather.data.repository

import com.piglet.weather.api.WeatherApiInterface
import com.piglet.weather.data.model.response.WeatherLatLongModelResponse
import com.piglet.weather.data.model.response.WeatherModelResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface WeatherRepository {
    fun getLatLong(location: String): Flow<WeatherLatLongModelResponse>
    fun getWeather(latitude: Double, longitude: Double, units: String): Flow<WeatherModelResponse>
}

class WeatherRepositoryImpl(
    private val weatherApiInterface: WeatherApiInterface
) : WeatherRepository {

    companion object {
        private const val ERROR_LOAD_DATA = "Data failed or not found"
    }

    override fun getLatLong(location: String): Flow<WeatherLatLongModelResponse> {
        return flow {
            val response = weatherApiInterface.getLatLong(location)
            val body = response.body()
            val result = if (response.isSuccessful && body != null && body.isNotEmpty()) {
                body.first()
            } else {
                error("$ERROR_LOAD_DATA: getLatLong")
            }
            emit(result)
        }
    }

    override fun getWeather(
        latitude: Double,
        longitude: Double,
        units: String
    ): Flow<WeatherModelResponse> {
        return flow {
            val response = weatherApiInterface.getWeather(latitude, longitude, units)
            val body = response.body()
            val result = if (response.isSuccessful && body != null) {
                body
            } else {
                error("$ERROR_LOAD_DATA: getWeather")
            }
            emit(result)
        }
    }
}
