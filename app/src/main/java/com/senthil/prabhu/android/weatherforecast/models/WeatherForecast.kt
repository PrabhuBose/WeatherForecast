package com.senthil.prabhu.android.weatherforecast.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class WeatherForecast(

    @SerializedName("list")
    val weatherForecastInfo: ArrayList<WeatherForecastInfo>,
    @SerializedName("city")
    val cityInfo: CityInfo

) : Serializable

data class WeatherForecastInfo(
    @SerializedName("dt") val dateLong: Long,
    @SerializedName("dt_txt") val dateText: String,
    @SerializedName("weather") val weather: ArrayList<WeatherInfo>,
    @SerializedName("main") val temperatureData: TemperatureInfo,
    @SerializedName("wind") val windData: WindInfo
)

data class WeatherInfo(
    @SerializedName("id") val id: Int,
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String

) : Serializable

data class TemperatureInfo(
    @SerializedName("temp") val temperature: Double,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("temp_min") val minTemperature: Double,
    @SerializedName("temp_max") val maxTemperature: Double

) : Serializable

data class WindInfo(
    @SerializedName("speed") val speed: Double,
    @SerializedName("deg") val deg: Int
) : Serializable


data class CityInfo(
    @SerializedName("name") val cityName: String,
    @SerializedName("country") val countryCode: String
) : Serializable

