package com.example.planto;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Weather_Data {

    private String mTemperature, micon, mcity, mWeatherType, mWeather_icon_Type, humidity, wind_speed;
    private int mCondition;

    public static Weather_Data fromJson(JSONObject jsonObject) {

        try {
            Weather_Data weatherD = new Weather_Data();
            weatherD.mcity = jsonObject.getString("name");
            weatherD.mCondition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.mWeatherType = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");

            weatherD.mWeather_icon_Type = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");
            weatherD.micon = updateWeatherIcon(weatherD.mWeather_icon_Type);

            Log.e("fuck me pal: ", "yo");

            double humidityResult = jsonObject.getJSONObject("main").getDouble("humidity");
            int humidity_roundedValue = (int) Math.rint(humidityResult);
            weatherD.humidity = Integer.toString(humidity_roundedValue);
            Log.e("fuck me pal: ", weatherD.humidity);

            double windResult = jsonObject.getJSONObject("wind").getDouble("speed");
            int wind_roundedValue = (int) Math.rint(windResult);
            weatherD.wind_speed = Integer.toString(wind_roundedValue);
            Log.e("fuck me pal: ", weatherD.wind_speed);


            double tempResult = jsonObject.getJSONObject("main").getDouble("temp") - 273.15;
            int temp_roundedValue = (int) Math.rint(tempResult);
            weatherD.mTemperature = Integer.toString(temp_roundedValue);
            return weatherD;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }


    private static String updateWeatherIcon(String icon) {
        String imageUrl = "https://openweathermap.org/img/w/" + icon + ".png";
        return imageUrl;
    }

    public String getmTemperature() {
        return mTemperature + "°C";
    }

    public String getMicon() {
        return micon;
    }

    public String getMcity() {
        return mcity;
    }

    public String getmWeatherType() {
        return mWeatherType;
    }

    public String humidity() {
        return "Humidity: " + humidity + "%";
    }

    public String wind_speed() {
        return "Wind: " + wind_speed + " mph";
    }

}
