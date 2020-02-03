package com.senthil.prabhu.android.weatherforecast.api

import android.content.Context
import com.google.gson.GsonBuilder
import com.senthil.prabhu.android.weatherforecast.R
import com.senthil.prabhu.android.weatherforecast.models.CurrentWeather
import com.senthil.prabhu.android.weatherforecast.models.WeatherForecast
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit

interface WeatherApi {


    @GET("weather?")
    fun getCurrentWeather(
        @Query("q") cityName: String,
        @Query("appid") appId: String,
        @Query("units") units: String

    ): Observable<CurrentWeather>

    @GET("forecast?")
    fun getWeatherForecast(
        @Query("lat") latitude: String,
        @Query("lon") longitude: String,
        @Query("appid") appId: String,
        @Query("units") units: String
    ): Observable<WeatherForecast>



}