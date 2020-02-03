package com.senthil.prabhu.android.weatherforecast.models

class DateHeadingKT : DataTypeKT() {

    private lateinit var groupName: String

    fun getGroupName(): String {
        return groupName
    }

    fun setGroupName(groupName: String) {
        this.groupName = groupName
    }


    override fun getDataType(): Int {
        return TYPE_GROUP_NAME
    }
}