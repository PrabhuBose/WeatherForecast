package com.senthil.prabhu.android.weatherforecast.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.senthil.prabhu.android.weatherforecast.R
import com.senthil.prabhu.android.weatherforecast.adapters.CurrentWeatherAdapter
import com.senthil.prabhu.android.weatherforecast.api.InvokeRetrofitService
import com.senthil.prabhu.android.weatherforecast.api.WeatherApi
import com.senthil.prabhu.android.weatherforecast.models.CurrentWeather
import com.senthil.prabhu.android.weatherforecast.utils.CustomProgressDialog
import com.senthil.prabhu.android.weatherforecast.utils.Utils
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import java.util.*
import kotlin.collections.ArrayList


class SearchByCityNames : AppCompatActivity(), View.OnClickListener {

    private var weatherData: ArrayList<CurrentWeather> = ArrayList()
    private lateinit var customProgressDialog: CustomProgressDialog


    private val client: WeatherApi by lazy {
        InvokeRetrofitService.create(this)
    }

    private lateinit var currentWeatherAdapter: CurrentWeatherAdapter
    private lateinit var cityNames: EditText

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        customProgressDialog = CustomProgressDialog(this@SearchByCityNames)

        cityNames = findViewById(R.id.city_name_et)

        findViewById<Button>(R.id.search).setOnClickListener(this)

        val currentWeatherRecyclerView: RecyclerView =
            findViewById(R.id.current_weather_recyclerview)

        currentWeatherAdapter = CurrentWeatherAdapter(weatherData)


        val itemDecorator =
            DividerItemDecoration(this@SearchByCityNames, DividerItemDecoration.HORIZONTAL)
        itemDecorator.setDrawable(
            Objects.requireNonNull<Drawable>(
                ContextCompat.getDrawable(
                    this@SearchByCityNames.applicationContext,
                    R.drawable.item_space
                )
            )
        )

        currentWeatherRecyclerView.addItemDecoration(itemDecorator)
        val layoutManager = LinearLayoutManager(
            this@SearchByCityNames,
            RecyclerView.VERTICAL, false
        )
        currentWeatherRecyclerView.layoutManager = layoutManager
        currentWeatherRecyclerView.adapter = currentWeatherAdapter


    }


    @SuppressLint("CheckResult")
    private fun getWeatherForCityName(cityNames: Array<String>) {
        customProgressDialog.show()
        hideKeyboard()
        weatherData.clear()

        val requests = ArrayList<Observable<CurrentWeather>>()

        val iterator = cityNames.iterator()
        iterator.forEach {

            if (it.trim().isNotEmpty()) {
                val observable: Observable<CurrentWeather> =
                    client.getCurrentWeather(
                        it.trim(),
                        resources.getString(R.string.open_weather_api_key),
                        resources.getString(R.string.units)
                    )
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())


                requests.add(observable)
            }
        }

        val disposable = Observable.fromIterable(requests)
            .flatMap { item ->
                Observable.create<Boolean> {
                    item.subscribe(
                        { result ->
                            customProgressDialog.hide()
                            combineWeatherData(result)
                        },
                        { error ->
                            customProgressDialog.hide()

                            if (error is HttpException) {
                                val httpException: HttpException = error
                                val body: ResponseBody? = httpException.response().errorBody()
                                val jsonObject = JSONObject(body!!.string())
                                Toast.makeText(
                                    this@SearchByCityNames,
                                    jsonObject.getString("message"),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        })
                    it.onNext(true)
                    it.onComplete()
                }

            }.subscribe()

        compositeDisposable.add(disposable)
    }


    fun combineWeatherData(currentWeather: CurrentWeather) {
        weatherData.add(currentWeather)
        currentWeatherAdapter.notifyDataSetChanged()
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.search -> {

                val cityNames: Array<String> = cityNames.text.toString().split(",").toTypedArray()
                if (cityNames.size < 3) {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.error_message_for_three_cities),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return
                }

                if (cityNames.size > 7) {
                    Toast.makeText(
                        this,
                        resources.getString(R.string.error_message_for_seven_cities),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    return
                }


                if (Utils.checkNetwork(this@SearchByCityNames))
                    getWeatherForCityName(cityNames)
                else
                    Toast.makeText(
                        this,
                        resources.getString(R.string.no_internet),
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }
    }

    fun AppCompatActivity.hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
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
