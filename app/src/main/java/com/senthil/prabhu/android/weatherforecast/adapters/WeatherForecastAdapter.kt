package com.senthil.prabhu.android.weatherforecast.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.senthil.prabhu.android.weatherforecast.R
import com.senthil.prabhu.android.weatherforecast.models.DataTypeKT
import com.senthil.prabhu.android.weatherforecast.models.DateHeadingKT
import com.senthil.prabhu.android.weatherforecast.models.WeatherDataKT
import com.senthil.prabhu.android.weatherforecast.models.WeatherForecastInfo
import com.senthil.prabhu.android.weatherforecast.utils.Utils

class WeatherForecastAdapter(
    private var consolidatedList: ArrayList<DataTypeKT> = ArrayList()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    inner class WeatherInfo internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var description: TextView = view.findViewById(R.id.description)
        internal var windSpeed: TextView = view.findViewById(R.id.wind_speed)
        internal var time: TextView = view.findViewById(R.id.time)
        internal var minTemp: TextView = view.findViewById(R.id.min_temp)
        internal var maxTemp: TextView = view.findViewById(R.id.max_temp)

    }

    inner class DateHeader internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        internal var date: TextView = view.findViewById(R.id.date)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)

        when (viewType) {
            DataTypeKT.TYPE_DATA -> {
                val v1: View = inflater.inflate(
                    R.layout.weather_forecast_data_item, parent,
                    false
                )
                viewHolder = WeatherInfo(v1)
            }

            DataTypeKT.TYPE_GROUP_NAME -> {
                val v2: View = inflater.inflate(
                    R.layout.weather_forecast_date_heading, parent,
                    false
                )
                viewHolder = DateHeader(v2)
            }


        }
        return viewHolder!!
    }

    override fun getItemCount(): Int {
        return consolidatedList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder.itemViewType) {
            DataTypeKT.TYPE_DATA -> {

                val monitorData: WeatherDataKT = consolidatedList[position] as WeatherDataKT
                val weatherInfoHolder: WeatherInfo = holder as WeatherInfo
                val weatherData: WeatherForecastInfo = monitorData.getModel()

                weatherInfoHolder.windSpeed.text = weatherData.windData.speed.toString()
                weatherInfoHolder.description.text = weatherData.weather[0].description
                weatherInfoHolder.minTemp.text =
                    "Min : " + weatherData.temperatureData.minTemperature + "\u2103"
                weatherInfoHolder.maxTemp.text =
                    "Max : " + weatherData.temperatureData.maxTemperature + "\u2103"
                weatherInfoHolder.time.text = Utils.convertTime(weatherData.dateLong)
            }
            DataTypeKT.TYPE_GROUP_NAME -> {
                val groupName: DateHeadingKT = consolidatedList[position] as DateHeadingKT
                val dateHeaderInfoHolder: DateHeader = holder as DateHeader

                dateHeaderInfoHolder.date.text = groupName.getGroupName()
            }

        }


    }


    override fun getItemViewType(position: Int): Int {
        return consolidatedList[position].getDataType()
    }

}