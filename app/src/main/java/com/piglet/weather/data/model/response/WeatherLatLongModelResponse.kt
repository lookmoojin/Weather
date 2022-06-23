package com.piglet.weather.data.model.response

import com.google.gson.annotations.SerializedName

data class WeatherLatLongModelResponse(
    @SerializedName("name")
    val name: String? = null,

    @SerializedName("lat")
    val latitude: Double? = null,

    @SerializedName("lon")
    val longitude: Double? = null,

    @SerializedName("country")
    val country: String? = null
)