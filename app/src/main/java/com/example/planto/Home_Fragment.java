package com.example.planto;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import cz.msebera.android.httpclient.Header;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Home_Fragment extends Fragment {
    final String APP_ID = "f88cf7dd4b8bce0b2247e84e8a71a6ee";
    final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";

    final long MIN_TIME = 5000;
    final float MIN_DISTANCE = 1000;
    final int REQUEST_CODE = 101;
    FirebaseFirestore db;
    FirebaseUser firebaseUser;
    User user;
    String Location_Provider = LocationManager.GPS_PROVIDER;
    Animation left, right, fade_in;

    TextView NameofCity, weatherState, Temperature, wind_speed, humidity, name;
    ImageView mweatherIcon, user_image;


    LocationManager mLocationManager;
    LocationListener mLocationListner;
    CardView card_view_1, card_view_2, card_view_3, card_view_4;
    LinearLayout mCityFinder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    public void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        card_view_1 = getActivity().findViewById(R.id.card_view_1);
        card_view_2 = getActivity().findViewById(R.id.card_view_2);
        card_view_3 = getActivity().findViewById(R.id.card_view_3);
        card_view_4 = getActivity().findViewById(R.id.card_view_4);

        weatherState = getActivity().findViewById(R.id.weatherCondition);
        Temperature = getActivity().findViewById(R.id.temperature);
        mweatherIcon = getActivity().findViewById(R.id.weatherIcon);
        mCityFinder = getActivity().findViewById(R.id.cityFinder);
        NameofCity = getActivity().findViewById(R.id.cityName);
        wind_speed = getActivity().findViewById(R.id.wind_speed);
        humidity = getActivity().findViewById(R.id.humidity);
        name = getActivity().findViewById(R.id.name);
        user_image = getActivity().findViewById(R.id.user_image);

        left = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.left);
        right = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.right);
        fade_in = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fade_in);

        // Set up the fade-in animation
        mCityFinder.setAlpha(0.0f);
        mCityFinder.setVisibility(View.VISIBLE);
        mCityFinder.animate()
                .alpha(1.0f)
                .setDuration(900)
                .setInterpolator(new AccelerateInterpolator())
                .start();
        card_view_1.setAnimation(left);
        card_view_2.setAnimation(right);
        card_view_3.setAnimation(left);
        card_view_4.setAnimation(right);

        name = getActivity().findViewById(R.id.name);
        name.setText("Straw Hats");

        mCityFinder.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity().getApplicationContext(), Weather_world.class);
            startActivity(intent);
        });
        getUserData();
    }


    @Override
    public void onResume() {
        super.onResume();
//        Intent mIntent = getActivity().getIntent();
//        String city = mIntent.getStringExtra("City");
//        if (city != null) {
//            getWeatherForNewCity(city);
//        } else {
        getWeatherForCurrentLocation();
//        }
    }


    private void getWeatherForNewCity(String city) {
        RequestParams params = new RequestParams();
        params.put("q", city);
        params.put("appid", APP_ID);
        letsdoSomeNetworking(params);

    }


    private void getWeatherForCurrentLocation() {

        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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


        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        mLocationManager.requestLocationUpdates(Location_Provider, MIN_TIME, MIN_DISTANCE, mLocationListner);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity().getApplicationContext(), "Locationget Succesffully", Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            } else {
                //user denied the permission
            }
        }


    }


    private void letsdoSomeNetworking(RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Toast.makeText(getActivity().getApplicationContext(), "Data Get Success", Toast.LENGTH_SHORT).show();

                Weather_Data weatherD = Weather_Data.fromJson(response);
                updateUI(weatherD);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            }
        });


    }

    private void updateUI(Weather_Data weather) {

        Temperature.setText(weather.getmTemperature());
        NameofCity.setText(weather.getMcity());
        weatherState.setText(weather.getmWeatherType());
        String imageUrl = weather.getMicon();

        Glide.with(getActivity().getApplicationContext())
                .load(imageUrl)
                .into(mweatherIcon);

        wind_speed.setText(weather.wind_speed());
        humidity.setText(weather.humidity());


    }

    private void getUserData() {
        db.collection("users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(com.google.firebase.firestore.DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
                if (user != null) {
                    name.setText(getFirstName(user.getUsername()));
                    Picasso.get().load(user.getAvatar_url()).into(user_image);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFirstName(String name) {
        String[] names = name.split(" ");
        return names[0];
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(mLocationListner);
        }
    }
}