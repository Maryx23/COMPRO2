import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.util.Scanner;

public class WeatherFetcher {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Latitude: ");
        double lat = scanner.nextDouble();

        System.out.print("Enter Longitude: ");
        double lon = scanner.nextDouble();

        scanner.close();

        String url = "https://www.7timer.info/bin/astro.php?lon=" + lon + "&lat=" + lat + "&ac=0&unit=metric&output=json";

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println(response.body());
            } else {
                System.out.println("Error: " + response.statusCode());
            }

        } catch (IOException e) {
            System.out.println("No internet connection.");
        } catch (InterruptedException e) {
            System.out.println("Request was interrupted.");
        }
    }
}