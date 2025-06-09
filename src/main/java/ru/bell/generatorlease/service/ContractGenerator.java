package ru.bell.generatorlease.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.bell.generatorlease.models.ContractModel;
import ru.bell.generatorlease.storage.CarStorage;
import ru.bell.generatorlease.storage.ClientStorage;

import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Slf4j
@Service
public class ContractGenerator {
    private final Sender sender;
    private final CarStorage carStorage;
    private final ClientStorage clientStorage;

    @Scheduled(initialDelay = 1555, fixedRate = 333)
    public void generateAndSend() {
        if (checksOK()) {
            generateContract();
        } else {
            System.out.println("car available: " + carStorage.hasNext());
            System.out.println("client available: " + clientStorage.hasNext());
        }

    }

    private void generateContract() {
        ContractModel contractModel = new ContractModel()
                .setCarVIN(carStorage.getNextAndRemove())
                .setClientId(Integer.parseInt(clientStorage.getNextAndRemove()))
                .setDurationInMonths(6)
                .setInitialPayment("0")
                .setInterestRate(new Random().nextInt(15, 35));
        String json = new Gson().toJson(contractModel, ContractModel.class);
        System.out.println(json);
    }

    private boolean checksOK() {
        return carStorage.hasNext() && clientStorage.hasNext();
    }

    @Scheduled(fixedRate = 10000)
    public void print() {
        System.out.println(carStorage.getList());
        System.out.println(clientStorage.getList());
    }
}
