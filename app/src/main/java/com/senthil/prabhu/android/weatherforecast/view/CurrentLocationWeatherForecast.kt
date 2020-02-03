package com.senthil.prabhu.android.weatherforecast.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.senthil.prabhu.android.weatherforecast.R
import com.senthil.prabhu.android.weatherforecast.adapters.WeatherForecastAdapter
import com.senthil.prabhu.android.weatherforecast.api.InvokeRetrofitService
import com.senthil.prabhu.android.weatherforecast.api.WeatherApi
import com.senthil.prabhu.android.weatherforecast.models.DataTypeKT
import com.senthil.prabhu.android.weatherforecast.models.WeatherForecast
import com.senthil.prabhu.android.weatherforecast.utils.CustomProgressDialog
import com.senthil.prabhu.android.weatherforecast.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class CurrentLocationWeatherForecast : AppCompatActivity() {

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val PERMISSIONID = 42
    private var location: Location? = null
    private lateinit var customProgressDialog: CustomProgressDialog
    private lateinit var weatherForecastData: WeatherForecast
    private lateinit var forecastWeatherRecyclerView: RecyclerView
    private lateinit var cityNameTV: TextView
    private lateinit var countryNameTV: TextView
    private lateinit var forecastWeatherAdapter: WeatherForecastAdapter

    private var finalGroupData: ArrayList<DataTypeKT> = ArrayList()

    private val client: WeatherApi by lazy {
        InvokeRetrofitService.create(this)
    }

    var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_location_weather_forecast)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        customProgressDialog = CustomProgressDialog(this@CurrentLocationWeatherForecast)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (Utils.checkNetwork(this@CurrentLocationWeatherForecast))
            getLastLocation()
        else
            Toast.makeText(
                this,
                resources.getString(R.string.no_internet),
                Toast.LENGTH_SHORT
            ).show()

        cityNameTV = findViewById(R.id.city_name)
        countryNameTV = findViewById(R.id.country_name)

        forecastWeatherRecyclerView =
            findViewById(R.id.forecast_weather_recyclerview)

        forecastWeatherAdapter = WeatherForecastAdapter(finalGroupData)


        val itemDecorator =
            DividerItemDecoration(
                this@CurrentLocationWeatherForecast,
                DividerItemDecoration.HORIZONTAL
            )
        itemDecorator.setDrawable(
            Objects.requireNonNull<Drawable>(
                ContextCompat.getDrawable(
                    this@CurrentLocationWeatherForecast.applicationContext,
                    R.drawable.item_space
                )
            )
        )

        forecastWeatherRecyclerView.addItemDecoration(itemDecorator)
        val layoutManager = LinearLayoutManager(
            this@CurrentLocationWeatherForecast,
            RecyclerView.VERTICAL, false
        )
        forecastWeatherRecyclerView.layoutManager = layoutManager
        forecastWeatherRecyclerView.adapter = forecastWeatherAdapter

    }


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    location = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        getWeatherForecast(
                            location!!.latitude.toString(),
                            location!!.longitude.toString()
                        )
                    }
                }
            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.turn_on_location),
                    Toast.LENGTH_LONG
                ).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }


    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }


    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSIONID
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }


    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }


    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            location = locationResult.lastLocation
            getWeatherForecast(location!!.latitude.toString(), location!!.longitude.toString())
        }
    }


    @SuppressLint("CheckResult", "SetTextI18n")
    private fun getWeatherForecast(latitude: String, longitude: String) {
        customProgressDialog.show()

        disposable = client.getWeatherForecast(
            latitude,
            longitude,
            resources.getString(R.string.open_weather_api_key),
            resources.getString(R.string.units)
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    customProgressDialog.hide()
                    weatherForecastData = result
                    finalGroupData.clear()
                    finalGroupData.addAll(Utils.groupDataIntoHashMap(weatherForecastData.weatherForecastInfo))
                    forecastWeatherAdapter.notifyDataSetChanged()

                    cityNameTV.text =
                        resources.getString(R.string.city_name) + weatherForecastData.cityInfo.cityName

                    countryNameTV.text =
                        resources.getString(R.string.country_name) + Utils.getCountryName(
                            weatherForecastData.cityInfo.countryCode
                        )


                },
                {
                    customProgressDialog.hide()
                }
            )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }


}
