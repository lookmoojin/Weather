package com.piglet.weather.domain.usecase

import android.content.Context
import com.piglet.weather.BuildConfig
import com.piglet.weather.R
import com.piglet.weather.data.model.response.WeatherModelResponse
import com.piglet.weather.data.repository.WeatherRepository
import com.piglet.weather.domain.DateTimeInterface
import com.piglet.weather.domain.model.WeatherCurrentModel
import com.piglet.weather.domain.model.WeatherDetailModel
import com.piglet.weather.domain.model.WeatherModel
import com.piglet.weather.domain.usecase.GetWeatherUseCaseImpl.Companion.UNIT_CELSIUS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.math.floor

interface GetWeatherUseCase {
    fun execute(
        location: String,
        latitude: Double,
        longitude: Double,
        units: String = UNIT_CELSIUS
    ): Flow<WeatherModel>
}

class GetWeatherUseCaseImpl(
    private val weatherRepository: WeatherRepository,
    private val dateTimeInterface: DateTimeInterface,
    private val context: Context
) : GetWeatherUseCase {

    companion object {
        private const val PERCENT = "%"
        private const val DEGREE = "°"
        private const val HOUR_OF_DAY_LIMIT = 25
        private const val HOUR_FORMAT = "HH"
        const val DOMAIN_ICON = BuildConfig.DOMAIN_IMAGE
        const val PNG = "@4x.png"
        const val UNIT_CELSIUS = "metric"
        const val UNIT_FAHRENHEIT = "imperial"
    }

    override fun execute(
        location: String,
        latitude: Double,
        longitude: Double,
        units: String
    ): Flow<WeatherModel> {
        return weatherRepository.getWeather(latitude, longitude, units)
            .map {
                mapToWeatherModel(it, location, units)
            }
    }

    private fun mapToWeatherModel(
        response: WeatherModelResponse,
        location: String,
        units: String
    ): WeatherModel {
        val current = response.current
        return WeatherModel(
            latitude = response.latitude,
            longitude = response.longitude,
            isCelsiusUnits = units == UNIT_CELSIUS,
            weatherCurrentModel = WeatherCurrentModel(
                temp = current?.temp?.let { "${floor(it).toInt()}$DEGREE" },
                description = current?.weather?.firstOrNull()?.description,
                icon = "$DOMAIN_ICON${current?.weather?.firstOrNull()?.icon}$PNG",
                time = current?.dateTime.toString(),
                location = location
            ),
            weatherDetailModelList = listOf(
                WeatherDetailModel(
                    title = context.resources.getString(R.string.humidity),
                    detail = current?.humidity?.let { "$it$PERCENT" }
                ),
                WeatherDetailModel(
                    title = context.resources.getString(R.string.feels_like),
                    detail = current?.feelsLike?.let { "${floor(it).toInt()}$DEGREE" }
                )
            ),
            weatherHourlyList = response.hourlyList?.take(HOUR_OF_DAY_LIMIT)?.map { hourly ->
                WeatherCurrentModel(
                    temp = hourly.temp?.let { "${floor(it).toInt()}$DEGREE" },
                    description = hourly.weather?.firstOrNull()?.description,
                    icon = "$DOMAIN_ICON${hourly.weather?.firstOrNull()?.icon}$PNG",
                    time = hourly.dateTime?.let { convertTime(it) }
                )
            }
        )
    }

    private fun convertTime(datetime: Long): String {
        val datetimeMillis = datetime * 1000
        return if (datetimeMillis <= dateTimeInterface.getCurrentTimeMillis()) {
            context.resources.getString(R.string.now)
        } else {
            dateTimeInterface.convertTimeMillisToDate(datetimeMillis, HOUR_FORMAT)
        }
    }
}