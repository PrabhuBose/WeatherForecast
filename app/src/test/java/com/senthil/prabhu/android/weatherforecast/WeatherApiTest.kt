package com.senthil.prabhu.android.weatherforecast

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.senthil.prabhu.android.weatherforecast.api.InvokeRetrofitService
import com.senthil.prabhu.android.weatherforecast.api.WeatherApi
import com.senthil.prabhu.android.weatherforecast.models.CurrentWeather
import com.senthil.prabhu.android.weatherforecast.models.WeatherForecast
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito

@RunWith(JUnit4::class)
class WeatherApiTest {
    @InjectMocks
    private lateinit var weatherApi: WeatherApi
    @Mock
    private lateinit var retrofitService: InvokeRetrofitService

    @Before
    fun setup() {
        print("Testing Started")
        retrofitService = mock()

    }

    @After
    fun finish() {
        print("Testing Finished")
    }

    @Test
    fun `get current weather api test`() {
        val cityName = "Dubai"
        val appid: String = mock()
        val units = "metric"
        weatherApi = Mockito.mock(WeatherApi::class.java)

        verify(weatherApi).getCurrentWeather(cityName, appid, units)
    }

    @Test
    fun `get  weather fore cast  api test`() {
        val latitude = "25.1677187"
        val longitude = "55.4062841"
        val appid: String = mock()
        val units = "metric"

        weatherApi = Mockito.mock(WeatherApi::class.java)

        verify(weatherApi).getWeatherForecast(latitude, longitude, appid, units)

    }
}