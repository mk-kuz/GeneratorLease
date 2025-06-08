package ru.bell.generatorlease;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class Sender {

    public void doSendCar(String VIN,String brand,String type,int year,String price) {
        CarModel carModel = new CarModel().setVIN(VIN).setBrand(brand).setType(type)
                .setYear(year).setPrice(price);
        String json = new Gson().toJson(carModel);
        send(json);
    }

    @SneakyThrows
    private void send(String json) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/cars/add"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(
                httpRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        System.out.println(response.body());
    }
}
