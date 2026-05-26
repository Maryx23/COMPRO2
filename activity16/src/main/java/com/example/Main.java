package com.example;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Longitude : ");
        double lon = sc.nextDouble();
        System.out.print("Enter Latitude : ");
        double lat = sc.nextDouble();

        WeatherService service = new WeatherService();

        try {
            WeatherResponse data = service.getForecast(lat, lon);
            if (data != null && data.forecast != null && !data.forecast.isEmpty()) {
                for (int i = 0; i < Math.min(3, data.forecast.size()); i++) {
                    Forecast f = data.forecast.get(i);
                    System.out.printf("At hour [%d]: %.2f°C with %d speed winds from the %s.\n",
                            f.timepoint, f.temperature, f.wind.getSpeed(), f.wind.getDirection());
                }
            } else {
                System.out.println("Could not retrieve weather data.");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Network error: Unable to connect to the weather service.");
        } finally {
            sc.close();
        }
    }
}