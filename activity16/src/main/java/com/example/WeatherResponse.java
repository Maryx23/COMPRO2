package com.example;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class WeatherResponse {
    protected String product;

    @SerializedName("dataseries")
    protected List<Forecast> forecast;
}