package ru.bell.generatorlease.service;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import ru.bell.generatorlease.models.CarModel;
import ru.bell.generatorlease.models.ClientModel;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class Sender {
    private static final String CARS_URI = "http://localhost:8080/api/cars/add";
    private static final String CLIENTS_URI = "http://localhost:8080/api/clients/add";
    private static final String CONTRACTS_URI = "http://localhost:8080/api/contracts/create";

    public int doSendCar(CarModel carModel) {
        String json = new Gson().toJson(carModel);
        System.out.println(json);
        return 200;
//        return sendTemplate(CARS_URI, json);
    }

    public int doSendClient(ClientModel clientModel) {
        String json = new Gson().toJson(clientModel);
        System.out.println(json);
        return 200;
//        return sendTemplate(CLIENTS_URI, json);
    }

    public int doSendContract(ClientModel clientModel) {
        String json = new Gson().toJson(clientModel);
        System.out.println(json);
        return 200;
//        return sendTemplate(CONTRACTS_URI, json);
    }

    @SneakyThrows
    private int sendTemplate(String uri, String json) {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/api/cli/" + uri))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(
                httpRequest, HttpResponse.BodyHandlers.ofString());
        return response.statusCode();
    }
}
