package com.example.lifebl.weather_forecast.tools;

import okhttp3.OkHttpClient;
import okhttp3.Request;
//执行Http请求
public class OkHttp {
  public static void sendHttpRequest(String address,okhttp3.Callback callback){
      OkHttpClient client=new OkHttpClient();
      Request request=new Request.Builder().url(address).build();
      client.newCall(request).enqueue(callback);
  }
}
