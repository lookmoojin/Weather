package com.piglet.weather.di

import com.piglet.weather.domain.DateTimeInterface
import com.piglet.weather.domain.DateTimeUtil
import com.piglet.weather.extension.CoroutineDispatcherProvider
import com.piglet.weather.extension.DefaultCoroutineDispatcherProvider
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

const val NAMED_SCALARS_CONVERTER = "named_scalars_converter"
const val NAMED_G_SON_CONVERTER = "named_gson_converter"

val networkModule = module {

    single<Converter.Factory>(named(NAMED_SCALARS_CONVERTER)) { ScalarsConverterFactory.create() }
    single<Converter.Factory>(named(NAMED_G_SON_CONVERTER)) { GsonConverterFactory.create() }

    factory<CoroutineDispatcherProvider> {
        DefaultCoroutineDispatcherProvider()
    }

    factory<DateTimeInterface> {
        DateTimeUtil
    }
}
