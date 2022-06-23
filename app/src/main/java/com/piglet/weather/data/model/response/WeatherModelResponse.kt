package com.piglet.weather.data.model.response

import com.google.gson.annotations.SerializedName

data class WeatherModelResponse(

    @SerializedName("lat")
    val latitude: Double? = null,

    @SerializedName("lon")
    val longitude: Double? = null,

    @SerializedName("current")
    val current: CurrentModelResponse? = null,

    @SerializedName("hourly")
    val hourlyList: List<CurrentModelResponse>? = null
)

data class CurrentModelResponse(
    @SerializedName("dt")
    val dateTime: Long? = null,

    @SerializedName("sunrise")
    val sunrise: Long? = null,

    @SerializedName("sunset")
    val sunset: Long? = null,

    @SerializedName("temp")
    val temp: Double? = null,

    @SerializedName("feels_like")
    val feelsLike: Double? = null,

    @SerializedName("humidity")
    val humidity: Int? = null,

    @SerializedName("weather")
    val weather: List<WeatherCurrentModelResponse>? = null
)

data class WeatherCurrentModelResponse(
    @SerializedName("main")
    val main: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("icon")
    val icon: String? = null
)