package ru.bell.generatorlease.service;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.bell.generatorlease.models.CarModel;
import ru.bell.generatorlease.models.ClientModel;
import ru.bell.generatorlease.models.ContractModel;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Slf4j
@Service
public class Sender {
    private static final String CARS_URI = "http://localhost:8080/api/cars/add";
    private static final String CLIENTS_URI = "http://localhost:8080/api/clients/add";
    private static final String CONTRACTS_URI = "http://localhost:8080/api/contracts/create";

    public int doSendCar(CarModel carModel) {
        String json = new Gson().toJson(carModel, CarModel.class);
        return sendTemplateGetCode(CARS_URI, json);
    }

    public Optional<HttpResponse<?>> doSendClient(ClientModel clientModel) {
        String json = new Gson().toJson(clientModel, ClientModel.class);
        Object o = sendTemplateGetResponse(json);
        if (o instanceof HttpResponse<?> response) {
            log.info(response.body().toString());
            return Optional.of(response);
        }
        System.out.println(o + " server error");
        return Optional.empty();
    }

    private Object sendTemplateGetResponse(String json) {
        return sendRequest(CLIENTS_URI, json);
    }

    public int doSendContract(ContractModel contractModel) {
        String json = new Gson().toJson(contractModel, ContractModel.class);
        return sendTemplateGetCode(CONTRACTS_URI, json);
    }

    @SneakyThrows
    private int sendTemplateGetCode(String uri, String json) {
        Object o = sendRequest(uri, json);
        if (o instanceof HttpResponse<?> response) {
            log.info(response.body().toString());
            return response.statusCode();
        }
        log.warn("{} server error", o);
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
