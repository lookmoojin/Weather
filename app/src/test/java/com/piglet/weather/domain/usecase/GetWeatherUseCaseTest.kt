package com.piglet.weather.domain.usecase

import android.content.Context
import android.content.res.Resources
import com.piglet.weather.R
import com.piglet.weather.data.model.response.CurrentModelResponse
import com.piglet.weather.data.model.response.WeatherCurrentModelResponse
import com.piglet.weather.data.model.response.WeatherModelResponse
import com.piglet.weather.data.repository.WeatherRepository
import com.piglet.weather.domain.DateTimeInterface
import com.piglet.weather.domain.usecase.GetWeatherUseCaseImpl.Companion.UNIT_FAHRENHEIT
import com.piglet.weather.extension.collectSafe
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

interface GetWeatherUseCaseTestCase {
    fun execute_mapData_returnWeatherModel()
    fun execute_mapData_isFahrenheitUnits_returnFalse()
    fun execute_mapData_currentNull_returnNull()
    fun execute_mapData_hourlyListNull_returnNull()
}

@ExperimentalCoroutinesApi
class GetWeatherUseCaseTest : GetWeatherUseCaseTestCase {

    @MockK
    lateinit var weatherRepository: WeatherRepository

    @MockK
    lateinit var dateTimeInterface: DateTimeInterface

    @MockK
    lateinit var context: Context

    @MockK
    lateinit var resources: Resources

    private lateinit var getWeatherUseCase: GetWeatherUseCase

    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        getWeatherUseCase = GetWeatherUseCaseImpl(
            weatherRepository,
            dateTimeInterface,
            context
        )
        every { context.resources } returns resources
        every { resources.getString(R.string.humidity) } returns "humidity"
        every { resources.getString(R.string.feels_like) } returns "feels_like"
        every { resources.getString(R.string.now) } returns "now"
        every { dateTimeInterface.getCurrentTimeMillis() } returns 11111111000
        every { dateTimeInterface.convertTimeMillisToDate(any(), any()) } returns "04"
    }

    @Test
    override fun execute_mapData_returnWeatherModel() = runTest {
        val mockResponse = WeatherModelResponse(
            latitude = 11.11,
            longitude = 22.22,
            current = CurrentModelResponse(
                temp = 35.80,
                dateTime = 11111111,
                humidity = 50,
                feelsLike = 38.50,
                weather = listOf(
                    WeatherCurrentModelResponse(
                        description = "description",
                        icon = "icon"
                    )
                )
            ),
            hourlyList = listOf(
                CurrentModelResponse(
                    temp = 33.00,
                    dateTime = 10000000,
                    weather = listOf(
                        WeatherCurrentModelResponse(
                            description = "description22",
                            icon = "icon22"
                        )
                    )
                ),
                CurrentModelResponse(
                    temp = 34.00,
                    dateTime = 33333333,
                    weather = listOf(
                        WeatherCurrentModelResponse(
                            description = "description33",
                            icon = "icon33"
                        )
                    )
                )
            )
        )
        coEvery {
            weatherRepository.getWeather(any(), any(), any())
        } returns flowOf(mockResponse)

        getWeatherUseCase.execute("bangkok", 12.34, 12.34)
            .collectSafe {
                assertEquals(11.11, it.latitude)
                assertEquals(22.22, it.longitude)
                assertEquals(true, it.isCelsiusUnits)
                assertEquals("35°", it.weatherCurrentModel?.temp)
                assertEquals("description", it.weatherCurrentModel?.description)
                assertEquals(
                    "${GetWeatherUseCaseImpl.DOMAIN_ICON}icon${GetWeatherUseCaseImpl.PNG}",
                    it.weatherCurrentModel?.icon
                )
                assertEquals("11111111", it.weatherCurrentModel?.time)
                assertEquals("bangkok", it.weatherCurrentModel?.location)
                assertEquals(2, it.weatherDetailModelList?.size)
                assertEquals("humidity", it.weatherDetailModelList?.first()?.title)
                assertEquals("50%", it.weatherDetailModelList?.first()?.detail)
                assertEquals("feels_like", it.weatherDetailModelList?.get(1)?.title)
                assertEquals("38°", it.weatherDetailModelList?.get(1)?.detail)
                assertEquals(2, it.weatherHourlyList?.size)
                assertEquals("33°", it.weatherHourlyList?.first()?.temp)
                assertEquals("description22", it.weatherHourlyList?.first()?.description)
                assertEquals(
                    "${GetWeatherUseCaseImpl.DOMAIN_ICON}icon22${GetWeatherUseCaseImpl.PNG}",
                    it.weatherHourlyList?.first()?.icon
                )
                assertEquals("now", it.weatherHourlyList?.first()?.time)
                assertEquals("34°", it.weatherHourlyList?.get(1)?.temp)
                assertEquals("description33", it.weatherHourlyList?.get(1)?.description)
                assertEquals(
                    "${GetWeatherUseCaseImpl.DOMAIN_ICON}icon33${GetWeatherUseCaseImpl.PNG}",
                    it.weatherHourlyList?.get(1)?.icon
                )
                assertEquals("04", it.weatherHourlyList?.get(1)?.time)
            }
    }

    @Test
    override fun execute_mapData_isFahrenheitUnits_returnFalse() = runTest {
        val mockResponse = WeatherModelResponse(
            latitude = 11.11,
            longitude = 22.22,
            current = null,
            hourlyList = null
        )
        coEvery {
            weatherRepository.getWeather(any(), any(), any())
        } returns flowOf(mockResponse)

        getWeatherUseCase.execute("bangkok", 12.34, 12.34, UNIT_FAHRENHEIT)
            .collectSafe {
                assertEquals(false, it.isCelsiusUnits)
            }
    }

    @Test
    override fun execute_mapData_currentNull_returnNull() = runTest {
        val mockResponse = WeatherModelResponse(
            latitude = 11.11,
            longitude = 22.22,
            current = null,
            hourlyList = listOf()
        )
        coEvery {
            weatherRepository.getWeather(any(), any(), any())
        } returns flowOf(mockResponse)

        getWeatherUseCase.execute("bangkok", 12.34, 12.34)
            .collectSafe {
                assertEquals(null, it.weatherCurrentModel?.temp)
                assertEquals(null, it.weatherCurrentModel?.description)
                assertEquals(
                    "${GetWeatherUseCaseImpl.DOMAIN_ICON}null${GetWeatherUseCaseImpl.PNG}",
                    it.weatherCurrentModel?.icon
                )
                assertEquals(2, it.weatherDetailModelList?.size)
                assertEquals("humidity", it.weatherDetailModelList?.first()?.title)
                assertEquals(null, it.weatherDetailModelList?.first()?.detail)
                assertEquals("feels_like", it.weatherDetailModelList?.get(1)?.title)
                assertEquals(null, it.weatherDetailModelList?.get(1)?.detail)
                assertEquals(0, it.weatherHourlyList?.size)
            }
    }

    @Test
    override fun execute_mapData_hourlyListNull_returnNull() = runTest {
        val mockResponse = WeatherModelResponse(
            latitude = 11.11,
            longitude = 22.22,
            current = null,
            hourlyList = null
        )
        coEvery {
            weatherRepository.getWeather(any(), any(), any())
        } returns flowOf(mockResponse)

        getWeatherUseCase.execute("bangkok", 12.34, 12.34)
            .collectSafe {
                assertEquals("null", it.weatherCurrentModel?.time)
                assertEquals(2, it.weatherDetailModelList?.size)
                assertEquals("humidity", it.weatherDetailModelList?.first()?.title)
                assertEquals(null, it.weatherDetailModelList?.first()?.detail)
                assertEquals("feels_like", it.weatherDetailModelList?.get(1)?.title)
                assertEquals(null, it.weatherDetailModelList?.get(1)?.detail)
                assertEquals(null, it.weatherHourlyList?.size)
            }
    }
}