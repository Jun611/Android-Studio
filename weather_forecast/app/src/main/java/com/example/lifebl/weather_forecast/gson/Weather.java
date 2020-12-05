package com.example.lifebl.weather_forecast.gson;

import com.google.gson.annotations.SerializedName;

public class Weather {
    @SerializedName("time")
    public String time;
    public CityInfo cityInfo;
    @SerializedName("date")
    public String date;
    @SerializedName("message")
    public String message;
    @SerializedName("status")
    public String status;
    public Data data;
}
