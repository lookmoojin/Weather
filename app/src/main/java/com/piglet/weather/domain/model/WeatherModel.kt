package com.piglet.weather.domain.model

data class WeatherModel(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isCelsiusUnits: Boolean = true,
    val weatherCurrentModel: WeatherCurrentModel? = null,
    val weatherDetailModelList: List<WeatherDetailModel>? = null,
    val weatherHourlyList: List<WeatherCurrentModel>? = null,
)

data class WeatherCurrentModel(
    val temp: String? = null,
    val description: String? = null,
    val icon: String? = null,
    val location: String? = null,
    val time: String? = null,
)

data class WeatherDetailModel(
    val title: String? = null,
    val detail: String? = null,
)
