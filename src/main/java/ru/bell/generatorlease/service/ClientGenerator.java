package ru.bell.generatorlease.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.bell.generatorlease.models.ClientModel;
import ru.bell.generatorlease.storage.ClientStorage;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.gson.JsonParser.parseString;

@RequiredArgsConstructor
@Service
public class ClientGenerator {
    private final Sender sender;
    private final ClientStorage storage;
    private final int CODE_OK = 200;
    private AtomicInteger counter;
    private List<String> names;

    @PostConstruct
    public void init() {
        constructLists();
    }

    @Scheduled(initialDelay = 500, fixedRate = 1300)
    public void generateAndSend() {
        ClientModel clientModel = new ClientModel()
                .setName(generateName())
                .setDocumentNumber(generateDocument())
                .setCellNumber(generateCellphone());
        Optional<HttpResponse<?>> sendClient = sender.doSendClient(clientModel);
        if (sendClient.isPresent() && sendClient.get().statusCode() == CODE_OK) {
            String body = (String) sendClient.get().body();
            JsonObject json = parseString(body).getAsJsonObject();
            int id = json.get("id").getAsInt();
            storage.add(id);
        }
    }

    private void constructLists() {
        names = new ArrayList<>();
        counter = new AtomicInteger(0);
        populateNames();
    }

    private String generateName() {
        return names.get(new Random()
                .nextInt(0, names.size()));
    }

    private String generateCellphone() {
        StringBuilder stringBuilder = new StringBuilder();
        if (counter.getAndIncrement() % 2 == 0) {
            stringBuilder.append("8916");
        } else {
            stringBuilder.append("8903");
        }
        if (counter.get() % 3 == 0) {
            stringBuilder.append("555");
        } else {
            stringBuilder.append("800");
        }
        int finisher = new Random().nextInt(1000, 9999);
        stringBuilder.append(finisher);
        return stringBuilder.toString();

    }

    private String generateDocument() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int newNum = new Random().nextInt(1, 10);
            stringBuilder.append(newNum);
        }
        return new String(stringBuilder);
    }

    private void populateNames() {
        names.add("Jack");
        names.add("Jane");
        names.add("Barbara");
        names.add("James");
        names.add("John");
        names.add("Bob");
        names.add("Peter");
        names.add("Mary");
    }
}
