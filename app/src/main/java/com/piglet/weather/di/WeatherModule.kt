package com.piglet.weather.di

import com.piglet.foundation.api.ApiGsonBuilder
import com.piglet.foundation.api.BaseHttpClient
import com.piglet.foundation.di.NAMED_G_SON_CONVERTER
import com.piglet.weather.BuildConfig
import com.piglet.weather.api.FeaturePathInterceptor
import com.piglet.weather.api.WeatherApiInterface
import com.piglet.weather.data.repository.CacheWeatherRepository
import com.piglet.weather.data.repository.CacheWeatherRepositoryImpl
import com.piglet.weather.data.repository.WeatherRepository
import com.piglet.weather.data.repository.WeatherRepositoryImpl
import com.piglet.weather.domain.usecase.GetLatLongUseCase
import com.piglet.weather.domain.usecase.GetLatLongUseCaseImpl
import com.piglet.weather.domain.usecase.GetWeatherUseCase
import com.piglet.weather.domain.usecase.GetWeatherUseCaseImpl
import com.piglet.weather.navigation.Router
import com.piglet.weather.navigation.router.SearchRouter
import com.piglet.weather.presentation.search.SearchFragment
import com.piglet.weather.presentation.search.SearchViewModel
import com.piglet.weather.presentation.weather.WeatherViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val weatherModule = module {

    single<WeatherApiInterface> {
        ApiGsonBuilder(
            okHttpClient = BaseHttpClient().create(FeaturePathInterceptor()),
            gsonConverterFactory = get(named(NAMED_G_SON_CONVERTER))
        ).build(BuildConfig.API_HOST_WEATHER)
    }

    factory<CacheWeatherRepository> {
        CacheWeatherRepositoryImpl()
    }

    factory<WeatherRepository> {
        WeatherRepositoryImpl(
            weatherApiInterface = get()
        )
    }

    factory<GetLatLongUseCase> {
        GetLatLongUseCaseImpl(
            weatherRepository = get()
        )
    }

    factory<GetWeatherUseCase> {
        GetWeatherUseCaseImpl(
            weatherRepository = get(),
            dateTimeInterface = get(),
            context = androidContext()
        )
    }

    viewModel {
        WeatherViewModel(
            coroutineDispatcher = get(),
            getLatLongUseCase = get(),
            getWeatherUseCase = get()
        )
    }

    scope<SearchFragment> {
        scoped<Router> { SearchRouter() }

        viewModel {
            SearchViewModel(
                router = get()
            )
        }
    }
}
