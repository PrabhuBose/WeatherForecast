package com.senthil.prabhu.android.weatherforecast.models

abstract class DataTypeKT {

    companion object {
        @JvmStatic

        public val TYPE_GROUP_NAME = 0
        public val TYPE_DATA = 1
    }


    abstract fun getDataType(): Int
}