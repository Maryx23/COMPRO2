package com.example;

import com.google.gson.annotations.SerializedName;

public class Forecast {
    protected int timepoint;

    @SerializedName("temp2m")
    protected double temperature;

    @SerializedName("wind10m")
    protected Wind wind;
}