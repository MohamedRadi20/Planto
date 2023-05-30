package com.example.planto;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WeatherViewHolder extends RecyclerView.ViewHolder {

    ImageView weatherIcon;
    TextView mDtTxt;
    TextView temperature;
    TextView weatherCondition;
    TextView cityName;
    TextView countryName;
    TextView lon;
    TextView lat;
    TextView rain;
    TextView humidity;
    TextView wind_speed;
    TextView Pressure;


    public WeatherViewHolder(@NonNull View itemView) {
        super(itemView);

        weatherIcon = itemView.findViewById(R.id.weatherIcon);
        mDtTxt = itemView.findViewById(R.id.mDtTxt);
        temperature = itemView.findViewById(R.id.temperature);
        weatherCondition = itemView.findViewById(R.id.weatherCondition);
        cityName = itemView.findViewById(R.id.cityName);
        countryName = itemView.findViewById(R.id.countryName);
        lon = itemView.findViewById(R.id.lon);
        lat = itemView.findViewById(R.id.lat);
        rain = itemView.findViewById(R.id.rain);
        humidity = itemView.findViewById(R.id.humidity);
        wind_speed = itemView.findViewById(R.id.wind_speed);
        Pressure = itemView.findViewById(R.id.Pressure);


    }
}
