package com.piglet.weather.domain.usecase

import com.piglet.weather.data.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

interface GetLatLongUseCase {
    fun execute(location: String?): Flow<Triple<String, Double, Double>>
}

class GetLatLongUseCaseImpl(
    private val weatherRepository: WeatherRepository
) : GetLatLongUseCase {

    companion object {
        private const val ERROR_LOCATION_BLANK = "Error location is blank"
    }

    override fun execute(location: String?): Flow<Triple<String, Double, Double>> {
        return if (location.isNullOrBlank()) {
            flow { emit(error(ERROR_LOCATION_BLANK)) }
        } else {
            weatherRepository.getLatLong(location)
                .filter {
                    it.latitude != null && it.longitude != null
                }
                .map {
                    Triple(it.name.orEmpty(), it.latitude!!, it.longitude!!)
                }
        }
    }
}