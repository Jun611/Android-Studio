package com.example.lifebl.weather_forecast.gson;

import com.google.gson.annotations.SerializedName;

public class Forecast {
    @SerializedName("date")
    public  String date;
    @SerializedName("ymd")
    public String ymd;
    @SerializedName("week")
    public  String week;
    @SerializedName("sunrise")
    public String sunrise;
    @SerializedName("high")
    public String high;
    @SerializedName("low")
    public String low;
    @SerializedName("sunset")
    public String sunset;
    @SerializedName("aqi")
    public String aqi;
    @SerializedName("fx")
    public String fx;
    @SerializedName("fl")
    public String fl;
    @SerializedName("type")
    public String type;
    @SerializedName("notice")
    public String notice;
}
