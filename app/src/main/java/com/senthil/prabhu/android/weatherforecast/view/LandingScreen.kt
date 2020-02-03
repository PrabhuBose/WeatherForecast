package com.senthil.prabhu.android.weatherforecast.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.senthil.prabhu.android.weatherforecast.R

class LandingScreen : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_screen)

        findViewById<Button>(R.id.search_by_city_name).setOnClickListener(this)
        findViewById<Button>(R.id.get_weather_forecast).setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.search_by_city_name -> {
                startActivity(
                    Intent(
                        this@LandingScreen,
                        SearchByCityNames::class.java
                    )
                )
            }

            R.id.get_weather_forecast -> {
                startActivity(
                    Intent(
                        this@LandingScreen,
                        CurrentLocationWeatherForecast::class.java
                    )
                )
            }
        }
    }
}
