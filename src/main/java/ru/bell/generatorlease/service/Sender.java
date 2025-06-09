package ru.bell.generatorlease.service;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.bell.generatorlease.models.CarModel;
import ru.bell.generatorlease.models.ClientModel;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.channels.ClosedChannelException;
import java.util.Arrays;
import java.util.Random;

@Slf4j
@Service
public class Sender {
    private static final String CARS_URI = "http://localhost:8080/api/cars/add";
    private static final String CLIENTS_URI = "http://localhost:8080/api/clients/add";
    private static final String CONTRACTS_URI = "http://localhost:8080/api/contracts/create";

    public int doSendCar(CarModel carModel) {
        String json = new Gson().toJson(carModel);

        return 200;
//        return sendTemplateGetCode(CARS_URI, json);
    }

    public int doSendClient(ClientModel clientModel) {
        String json = new Gson().toJson(clientModel, ClientModel.class);

        return  new Random().nextInt(9999);
//        Object o = sendTemplateGetResponse(CLIENTS_URI, json);
//        if (o instanceof HttpResponse) {
//            System.out.println(((HttpResponse<?>) o).statusCode());
//
//        } else {
//            System.out.println(o + " server error");
//
//        }
    }

    public int doSendContract(ClientModel clientModel) {
        String json = new Gson().toJson(clientModel);
        System.out.println(json);
        return 200;
//        return sendTemplateGetCode(CONTRACTS_URI, json);
    }

    private Object sendTemplateGetResponse(String uri, String json) {
        return sendRequest(uri, json);
    }

    @SneakyThrows
    private int sendTemplateGetCode(String uri, String json) {
        Object o = sendRequest(uri, json);
        if (o instanceof HttpResponse) {
            HttpResponse<?> response = (HttpResponse<?>) sendRequest(uri, json);
            return response.statusCode();
        }
        return HttpStatus.BAD_GATEWAY.value();
    }

    private Object sendRequest(String uri, String json) {
        try (HttpClient client = HttpClient.newHttpClient()) {
            return client.send(generateRequest(uri, json), HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @SneakyThrows
    private HttpRequest generateRequest(String uri, String json) {
        return HttpRequest.newBuilder()
                .uri(new URI(uri))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
    }
}
