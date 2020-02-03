package com.senthil.prabhu.android.weatherforecast.models

class WeatherDataKT : DataTypeKT() {

    private lateinit var model: WeatherForecastInfo

    fun getModel(): WeatherForecastInfo {
        return model
    }

    fun setModel(model: WeatherForecastInfo) {
        this.model = model
    }


    override fun getDataType(): Int {
        return TYPE_DATA
    }
}