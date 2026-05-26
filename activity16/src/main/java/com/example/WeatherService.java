package com.example;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherService {
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public WeatherResponse getForecast(double lat, double lon) throws IOException, InterruptedException {
        String url = String.format(
                "https://www.7timer.info/bin/astro.php?lon=%f&lat=%f&ac=0&unit=metric&output=json",
                lon, lat);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        System.out.println("Sending request...");

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return gson.fromJson(response.body(), WeatherResponse.class);
        }

        return null;
    }
}