package com.example.planto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

public class Weather_world extends AppCompatActivity {
    final String APP_ID = "f88cf7dd4b8bce0b2247e84e8a71a6ee";
    final String WEATHER_5Days_forecast_URL = "https://api.openweathermap.org/data/2.5/forecast";

    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;

    String Location_Provider = LocationManager.GPS_PROVIDER ,newCity;

    LocationManager mLocationManager;
    LocationListener mLocationListner;
    TextView darling;

    RecyclerView weatherRecyclerView;
    WeatherAdapter weatherAdapter;
    static ProgressBar progressBar;

    GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_world);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText editText = findViewById(R.id.searchCity);
        final Button submit_btn = findViewById(R.id.submit_btn);

        weatherRecyclerView = findViewById(R.id.weather_recycler_view);
        progressBar = findViewById(R.id.loading);
        darling = findViewById(R.id.darling);

        Paint paint = darling.getPaint();

        Shader shader = paint.setShader(new LinearGradient(0, 0, darling.getPaint().measureText(darling.getText().toString()), darling.getTextSize(),
                new int[]{Color.parseColor("#9BE6AD"), Color.parseColor("#039469")},
                new float[]{0, 1}, Shader.TileMode.CLAMP));


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        weatherRecyclerView.setLayoutManager(layoutManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        weatherRecyclerView.setItemAnimator(itemAnimator);

        weatherAdapter = new WeatherAdapter();
        weatherRecyclerView.setAdapter(weatherAdapter);

        submit_btn.setOnClickListener(v -> {
            newCity = editText.getText().toString();

            if (newCity != null) {
                getWeatherForNewCity(newCity);
            } else {
                getWeatherForCurrentLocation();
            }

        });

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                int nextItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition() + 1;

                if (nextItemPosition >= weatherAdapter.getItemCount()) {
                    nextItemPosition = 0;
                }

                weatherRecyclerView.smoothScrollToPosition(nextItemPosition);
            }

        };

        timer.schedule(timerTask, 0, 4000);


        graph = findViewById(R.id.graph);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(40);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(100);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setScalable(true);


        graph.setTitle("Weather Data");
        graph.setTitleColor(Color.BLACK);
        graph.setTitleTextSize(24);
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setTextColor(Color.BLACK);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitleColor(Color.BLACK);
        gridLabel.setVerticalAxisTitleColor(Color.BLACK);
        gridLabel.setVerticalAxisTitle("Values (enjoy the pattern :)");
        gridLabel.setHorizontalAxisTitle("Time every 3h");
        gridLabel.setGridColor(Color.parseColor("#ECECEC"));
        gridLabel.setHighlightZeroLines(false);


    }


    @Override
    protected void onResume() {
        super.onResume();
        getWeatherForCurrentLocation();
    }

    private void getWeatherForNewCity(String city) {
        RequestParams params = new RequestParams();
        params.put("q", city);
        params.put("appid", APP_ID);
        letsdoSomeNetworking(params);

    }

    private void getWeatherForCurrentLocation() {

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListner = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

                RequestParams params = new RequestParams();
                params.put("lat", Latitude);
                params.put("lon", Longitude);
                params.put("appid", APP_ID);
                letsdoSomeNetworking(params);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                //not able to get location
            }
        };


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        mLocationManager.requestLocationUpdates(Location_Provider, MIN_TIME, MIN_DISTANCE, mLocationListner);

    }

    private void letsdoSomeNetworking(RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_5Days_forecast_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Toast.makeText(Weather_world.this, "Data Get Success", Toast.LENGTH_SHORT).show();

                Weather_5Days_forecast.fromJson(response, weatherAdapter,graph);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(mLocationListner);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.feedback:
                Toast.makeText(getApplicationContext(), "Coming soon",
                        Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return true;
    }
}