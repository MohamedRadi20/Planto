package com.example.planto;

public class Weather {
    private String mCity, mCountry, mWeatherDescription, mIconUrl, mDtTxt;
    private int mTemperature, mPressure, mHumidity, mWindSpeed, mRain;
    private double mLatitude, mLongitude;
    private long mTimestamp;

    public Weather(String mCity, String mCountry, String mWeatherDescription, String mIconUrl, String mDtTxt, int mTemperature, int mPressure, int mHumidity, int mWindSpeed, int mRain, double mLatitude, double mLongitude) {
        this.mCity = mCity;
        this.mCountry = mCountry;
        this.mWeatherDescription = mWeatherDescription;
        this.mIconUrl = mIconUrl;
        this.mDtTxt = mDtTxt;
        this.mTemperature = mTemperature;
        this.mPressure = mPressure;
        this.mHumidity = mHumidity;
        this.mWindSpeed = mWindSpeed;
        this.mRain = mRain;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mTimestamp = mTimestamp;
    }

    public String getmCity() {
        return mCity;
    }

    public String getmCountry() {
        return mCountry;
    }

    public String getmWeatherDescription() {
        return mWeatherDescription;
    }

    public String getmIconUrl() {
        return mIconUrl;
    }

    public String getmDtTxt() {
        return mDtTxt;
    }

    public int getmTemperature() {
        return mTemperature;
    }

    public int getmPressure() {
        return mPressure;
    }

    public int getmHumidity() {
        return mHumidity;
    }

    public int getmWindSpeed() {
        return mWindSpeed;
    }

    public int getmRain() {
        return mRain;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

}
