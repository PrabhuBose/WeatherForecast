package com.senthil.prabhu.android.weatherforecast.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import com.senthil.prabhu.android.weatherforecast.models.DataTypeKT
import com.senthil.prabhu.android.weatherforecast.models.DateHeadingKT
import com.senthil.prabhu.android.weatherforecast.models.WeatherDataKT
import com.senthil.prabhu.android.weatherforecast.models.WeatherForecastInfo
import java.text.SimpleDateFormat
import java.util.*

class Utils {

    companion object {
        @SuppressLint("SimpleDateFormat")
        @JvmStatic

        fun convertDate(longDate: Long): String {

            val time = longDate * 1000.toLong()
            val date = Date(time)
            val format = SimpleDateFormat("dd MMM yyyy")
            format.timeZone = TimeZone.getTimeZone("GMT")

            return format.format(date)
        }

        @SuppressLint("SimpleDateFormat")
        fun convertTime(time: Long): String {
            val temp = time * 1000.toLong()
            val date = Date(temp)
            val format = SimpleDateFormat("HH:mm")
            format.timeZone = TimeZone.getTimeZone("GMT")
            return format.format(date)
        }


        fun groupDataIntoHashMap(trackMonitorDataList: List<WeatherForecastInfo>): List<DataTypeKT> {

            val groupedHashMap: HashMap<String, MutableList<WeatherForecastInfo>?> = HashMap()

            for (model in trackMonitorDataList) {
                val hashMapKey: String = convertDate(model.dateLong)
                if (groupedHashMap.containsKey(hashMapKey)) {
                    groupedHashMap[hashMapKey]!!.add(model)
                } else {
                    val list: MutableList<WeatherForecastInfo> = ArrayList()
                    list.add(model)
                    groupedHashMap[hashMapKey] = list
                }
            }
            val map: Map<String, MutableList<WeatherForecastInfo>?> =
                TreeMap(
                    groupedHashMap
                )
            val finalGroupData: MutableList<DataTypeKT> = ArrayList()
            for (group in map.keys) {

                val groupName = DateHeadingKT()
                groupName.setGroupName(group)
                finalGroupData.add(groupName)

                for (trackMonitorData in map[group] as List<WeatherForecastInfo>) {
                    val generalItem = WeatherDataKT()
                    generalItem.setModel(trackMonitorData)
                    finalGroupData.add(generalItem)
                }
            }
            return finalGroupData
        }

        fun getCountryName(countryCode: String): String {
            return Locale("", countryCode).displayName
        }


        fun checkNetwork(context: Context): Boolean {
            var haveConnectedWifi = false
            var haveConnectedMobile = false
            val cm =
                (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
            val netInfo = cm.allNetworkInfo
            for (ni in netInfo) {
                if (ni.typeName.equals(
                        "WIFI",
                        ignoreCase = true
                    )
                ) if (ni.isConnected) haveConnectedWifi = true
                if (ni.typeName.equals(
                        "MOBILE",
                        ignoreCase = true
                    )
                ) if (ni.isConnected) haveConnectedMobile = true
            }
            return haveConnectedWifi || haveConnectedMobile
        }

    }


}