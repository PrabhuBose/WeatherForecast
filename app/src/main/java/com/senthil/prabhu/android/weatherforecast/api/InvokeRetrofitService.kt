package com.senthil.prabhu.android.weatherforecast.api

import android.content.Context
import com.google.gson.GsonBuilder
import com.senthil.prabhu.android.weatherforecast.R
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.util.concurrent.TimeUnit

interface InvokeRetrofitService {

    companion object {
        private var mClient: OkHttpClient? = null
        private var mGsonConverter: GsonConverterFactory? = null

        private val gsonConverter: GsonConverterFactory
            get() {
                if (mGsonConverter == null) {
                    mGsonConverter = GsonConverterFactory
                        .create(
                            GsonBuilder()
                                .setLenient()
                                .disableHtmlEscaping()
                                .create()
                        )
                }
                return mGsonConverter!!
            }

        private val client: OkHttpClient
            @Throws(NoSuchAlgorithmException::class, KeyManagementException::class)
            get() {
                if (mClient == null) {
                    val interceptor = HttpLoggingInterceptor()
                    interceptor.level = HttpLoggingInterceptor.Level.BODY

                    val httpBuilder = OkHttpClient.Builder()
                    httpBuilder
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(20, TimeUnit.SECONDS)
                        .addInterceptor(interceptor)  /// show all JSON in logCat
                    mClient = httpBuilder.build()

                }
                return mClient!!
            }


        fun create(context: Context?): WeatherApi {
            val retrofit = Retrofit.Builder()
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(gsonConverter)
                .baseUrl(context!!.resources.getString(R.string.open_weather_base_url))
                .build()


            return retrofit.create(WeatherApi::class.java)
        }
    }
}