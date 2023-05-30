package com.example.planto;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

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
    static LineGraphSeries<DataPoint> series_temperature, series_humidity, series_pressure,series_rain,series_mWindSpeed ;


    public static void fromJson(JSONObject jsonObject, WeatherAdapter weatherAdapter, GraphView graph) {
        try {
            List<Weather> weatherList = new ArrayList<>();
            JSONArray listArray = jsonObject.getJSONArray("list");
            series_temperature = new LineGraphSeries<>();
            series_humidity = new LineGraphSeries<>();
            series_pressure = new LineGraphSeries<>();
            series_rain = new LineGraphSeries<>();
            series_mWindSpeed= new LineGraphSeries<>();

            series_temperature.setTitle("Temperature");
            series_humidity.setTitle("Humidity");
            series_pressure.setTitle("Pressure");
            series_rain.setTitle("Rain");
            series_mWindSpeed.setTitle("Wind Speed");


            series_temperature.setColor(Color.rgb(255, 0, 0));
            series_temperature.setDataPointsRadius(12);
            series_temperature.setThickness(6);
            series_temperature.setDrawDataPoints(true);
            series_temperature.setAnimated(true);
            series_temperature.setDrawAsPath(false);

            series_humidity.setColor(Color.rgb(0, 0, 255));
            series_humidity.setDataPointsRadius(12);
            series_humidity.setThickness(6);
            series_humidity.setDrawDataPoints(true);
            series_humidity.setAnimated(true);
            series_humidity.setDrawAsPath(false);

            series_pressure.setColor(Color.rgb(0, 255, 0));
            series_pressure.setDataPointsRadius(12);
            series_pressure.setThickness(6);
            series_pressure.setDrawDataPoints(true);
            series_pressure.setAnimated(true);
            series_pressure.setDrawAsPath(false);

            series_rain.setColor(Color.rgb(0, 191, 255));
            series_rain.setDataPointsRadius(12);
            series_rain.setThickness(6);
            series_rain.setDrawDataPoints(true);
            series_rain.setAnimated(true);
            series_rain.setDrawAsPath(false);

            series_mWindSpeed.setColor(Color.rgb(255, 215, 0));
            series_mWindSpeed.setDataPointsRadius(12);
            series_mWindSpeed.setThickness(6);
            series_mWindSpeed.setDrawDataPoints(true);
            series_mWindSpeed.setAnimated(true);
            series_mWindSpeed.setDrawAsPath(false);


            graph.addSeries(series_temperature);
            graph.addSeries(series_humidity);
            graph.addSeries(series_pressure);
            graph.addSeries(series_rain);
            graph.addSeries(series_mWindSpeed);

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

                series_temperature.appendData(new DataPoint(i, mTemperature), true,listArray.length());
                series_humidity.appendData(new DataPoint(i, mHumidity), true,listArray.length());
                series_pressure.appendData(new DataPoint(i, mapValue(mPressure,0,2000,0,100)), true,listArray.length());
                series_rain.appendData(new DataPoint(i, mapValue(mRain,0,10,0,100)), true,listArray.length());
                series_mWindSpeed.appendData(new DataPoint(i, mapValue(mWindSpeed,0,10,0,100)), true,listArray.length());

                Weather_world.progressBar.setVisibility(View.GONE);
                graph.setVisibility(View.VISIBLE);

                weatherList.add(new Weather(mCity, mCountry, mWeatherDescription, mIconUrl, mDtTxt, mTemperature, mPressure, mHumidity, mWindSpeed, mRain, mLatitude, mLongitude));

                weatherAdapter.setData(weatherList);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static double mapValue(double value, double inMin, double inMax, double outMin, double outMax) {
        return (value - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

}