package com.example.planto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Weather_5Days_forecast {

    private static String mCity, mCountry, mWeatherDescription, mIconUrl, mDtTxt;
    private static int mTemperature;
    private static int mPressure;
    private static int mHumidity;
    private static int mWindSpeed;
    private static int mRain;
    private static double mLatitude, mLongitude;

    public static void fromJson(JSONObject jsonObject, WeatherAdapter weatherAdapter) {
        try {
            List<Weather> weatherList = new ArrayList<>();
            JSONArray listArray = jsonObject.getJSONArray("list");

            for (int i = 0; i < listArray.length(); i++) {

                JSONObject object = listArray.getJSONObject(i);
                JSONObject cityObject = jsonObject.getJSONObject("city");
                mCity = cityObject.getString("name");
                mCountry = cityObject.getString("country");
                JSONObject coordObject = cityObject.getJSONObject("coord");
                mLatitude = coordObject.getDouble("lat");
                mLongitude = coordObject.getDouble("lon");

                JSONObject mainObject = object.getJSONObject("main");
                mTemperature = (int) Math.round(mainObject.getDouble("temp") - 273.15);
                mPressure = mainObject.getInt("pressure");
                mHumidity = mainObject.getInt("humidity");

                JSONObject windObject = object.getJSONObject("wind");
                mWindSpeed = (int) Math.round(windObject.getDouble("speed"));

                JSONArray weatherArray = object.getJSONArray("weather");
                JSONObject weatherObject = weatherArray.getJSONObject(0);
                mWeatherDescription = weatherObject.getString("description");
                mIconUrl = "https://openweathermap.org/img/w/" + weatherObject.getString("icon") + ".png";

                mDtTxt = object.getString("dt_txt");

                if (object.has("rain")) {
                    JSONObject rainObject = object.getJSONObject("rain");
                    if (rainObject.has("3h")) {
                        mRain = (int) Math.round(rainObject.getDouble("3h"));
                    }
                }
                weatherList.add(new Weather(mCity, mCountry, mWeatherDescription, mIconUrl, mDtTxt, mTemperature, mPressure, mHumidity, mWindSpeed, mRain, mLatitude, mLongitude));

                weatherAdapter.setData(weatherList);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}