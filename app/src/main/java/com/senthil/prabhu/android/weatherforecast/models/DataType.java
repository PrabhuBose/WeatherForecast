package com.senthil.prabhu.android.weatherforecast.models;

public abstract class DataType {

    public static final int TYPE_GROUP_NAME = 0;
    public static final int TYPE_DATA = 1;

    abstract public int getDataType();

}
