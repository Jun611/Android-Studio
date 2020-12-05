package com.example.lifebl.weather_forecast.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
    @SerializedName("shidu")
    public String shidu;
    @SerializedName("pm25")
    public String pm25;
    @SerializedName("pm10")
    public String pm10;
    @SerializedName("quality")
    public String quality;
    @SerializedName("wendu")
    public String wendu;
    @SerializedName("ganmao")
    public String ganmao;
    public Yesterday yesterday;
    @SerializedName("forecast")
    public List<Forecast> forecast;

}
