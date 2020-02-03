package com.senthil.prabhu.android.weatherforecast.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.senthil.prabhu.android.weatherforecast.R
import com.senthil.prabhu.android.weatherforecast.models.CurrentWeather
import com.senthil.prabhu.android.weatherforecast.utils.Utils

class CurrentWeatherAdapter(
    private var currentWeatherDataList: ArrayList<CurrentWeather>
) : RecyclerView.Adapter<CurrentWeatherAdapter.ViewHolder>() {


    inner class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var description: TextView = view.findViewById(R.id.description)
        internal var windSpeed: TextView = view.findViewById(R.id.wind_speed)
        internal var date: TextView = view.findViewById(R.id.date)
        internal var cityName: TextView = view.findViewById(R.id.city_name)
        internal var minTemp: TextView = view.findViewById(R.id.min_temp)
        internal var maxTemp: TextView = view.findViewById(R.id.max_temp)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.current_weather_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return currentWeatherDataList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentWeather: CurrentWeather = currentWeatherDataList[position]

        holder.cityName.text = currentWeather.cityName
        holder.windSpeed.text = currentWeather.windData.speed.toString()
        holder.description.text = currentWeather.weather[0].description
        holder.minTemp.text = "Min : " + currentWeather.temperatureData.minTemperature + "\u2103"
        holder.maxTemp.text = "Max : " + currentWeather.temperatureData.maxTemperature + "\u2103"
        holder.date.text = Utils.convertDate(currentWeather.date)
    }

}