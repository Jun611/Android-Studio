package com.example.lifebl.weather_forecast.gson;

import com.google.gson.annotations.SerializedName;

public class CityInfo {
    @SerializedName("city")
    public String cityname;
    @SerializedName("cityId")
    public String cityId;
    @SerializedName("parent")
    public String parent;
    @SerializedName("updateTime")
    public String updateTime;
}
