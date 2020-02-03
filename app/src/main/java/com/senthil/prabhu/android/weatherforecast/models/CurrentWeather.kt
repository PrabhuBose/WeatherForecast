package com.senthil.prabhu.android.weatherforecast.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CurrentWeather(
    @SerializedName("weather")
    val weather: ArrayList<WeatherData>,
    @SerializedName("main")
    val temperatureData: TemperatureData,
    @SerializedName("wind")
    val windData: WindData,
    @SerializedName("name")
    val cityName: String,
    @SerializedName("dt")
    val date: Long

) : Serializable

data class WeatherData(
    @SerializedName("id") val id: Int,
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String

) : Serializable

data class TemperatureData(
    @SerializedName("temp") val temperature: Double,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("temp_min") val minTemperature: Double,
    @SerializedName("temp_max") val maxTemperature: Double

) : Serializable

data class WindData(
    @SerializedName("speed") val speed: Double,
    @SerializedName("deg") val deg: Int
) : Serializable


