package com.example.planto;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherViewHolder> {

    private List<Weather> WeatherList = new ArrayList<>();

    public void setData(List<Weather> WeatherList) {
        this.WeatherList = WeatherList;
        notifyDataSetChanged();
    }

    public List<Weather> getData() {
        return WeatherList;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_list_item, parent, false);
        return new WeatherViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {

        Weather weather = WeatherList.get(position);
        Glide.with(holder.itemView.getContext()).load(weather.getmIconUrl()).into(holder.weatherIcon);
        holder.mDtTxt.setText(String.valueOf(weather.getmDtTxt()));
        holder.temperature.setText(weather.getmTemperature() + "°C");
        holder.weatherCondition.setText(String.valueOf(weather.getmWeatherDescription()));
        holder.cityName.setText(weather.getmCity() + ", ");
        holder.countryName.setText(String.valueOf(weather.getmCountry()));
        holder.lon.setText("Lon: "+weather.getmLongitude()+"°");
        holder.lat.setText("Lat: " + weather.getmLatitude()+"°");
        holder.rain.setText("Rain:\n " + weather.getmRain());
        holder.humidity.setText("Humidity:\n " + weather.getmHumidity()+"%");
        holder.wind_speed.setText("Wind:\n " + weather.getmWindSpeed()+" mph");
        holder.Pressure.setText("Pressure:\n " + weather.getmPressure()+" hPa");


    }

    @Override
    public int getItemCount() {
        return WeatherList.size();
    }
}
