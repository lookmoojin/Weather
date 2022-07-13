package com.piglet.weather.presentation.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.piglet.foundation.base.ScopedViewModel
import com.piglet.foundation.base.SingleLiveEvent
import com.piglet.weather.domain.model.WeatherModel
import com.piglet.weather.domain.usecase.GetLatLongUseCase
import com.piglet.weather.domain.usecase.GetWeatherUseCase
import com.piglet.weather.domain.usecase.GetWeatherUseCaseImpl.Companion.UNIT_CELSIUS
import com.piglet.weather.domain.usecase.GetWeatherUseCaseImpl.Companion.UNIT_FAHRENHEIT
import com.piglet.foundation.extension.CoroutineDispatcherProvider
import com.piglet.foundation.extension.collectSafe
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val coroutineDispatcher: CoroutineDispatcherProvider,
    private val getLatLongUseCase: GetLatLongUseCase,
    private val getWeatherUseCase: GetWeatherUseCase
) : ScopedViewModel() {

    private val display = SingleLiveEvent<WeatherModel>()
    private val showLoading = SingleLiveEvent<Unit>()
    private val hideLoading = SingleLiveEvent<Unit>()
    private val error = SingleLiveEvent<Unit>()
    fun onDisplay(): LiveData<WeatherModel> = display
    fun onShowLoading(): LiveData<Unit> = showLoading
    fun onHideLoading(): LiveData<Unit> = hideLoading
    fun onError(): LiveData<Unit> = error

    fun loadWeatherData(location: String?) {
        viewModelScope.launch {
            getLatLongUseCase.execute(location)
                .flowOn(coroutineDispatcher.io())
                .onStart {
                    showLoading.value = Unit
                }
                .flatMapConcat { (location, lat, lon) ->
                    getWeatherUseCase.execute(location, lat, lon)
                }
                .catch {
                    error.value = Unit
                }
                .onCompletion {
                    hideLoading.value = Unit
                }
                .collectSafe {
                    display.value = it
                }
        }
    }

    fun changeUnits(weatherModel: WeatherModel) {
        viewModelScope.launch {
            getWeatherUseCase.execute(
                location = weatherModel.weatherCurrentModel?.location.orEmpty(),
                latitude = weatherModel.latitude ?: 0.0,
                longitude = weatherModel.longitude ?: 0.0,
                units = if (weatherModel.isCelsiusUnits) UNIT_CELSIUS else UNIT_FAHRENHEIT
            )
                .flowOn(coroutineDispatcher.io())
                .catch {
                    error.value = Unit
                }
                .collectSafe {
                    display.value = it
                }
        }
    }
}
