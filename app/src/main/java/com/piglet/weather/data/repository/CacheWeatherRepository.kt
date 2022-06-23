package com.piglet.weather.data.repository

interface CacheWeatherRepository {
    fun saveLatLong(lat: Long, lon: Long)
    fun getLatLong(): Pair<Long?, Long?>
}

class CacheWeatherRepositoryImpl : CacheWeatherRepository {
    companion object {
        private var latitude: Long? = null
        private var longitude: Long? = null
    }

    override fun saveLatLong(lat: Long, lon: Long) {
        latitude = lat
        longitude = lon
    }

    override fun getLatLong(): Pair<Long?, Long?> {
        return Pair(latitude, longitude)
    }

}