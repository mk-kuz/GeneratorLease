package ru.bell.generatorlease.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.bell.generatorlease.models.ContractModel;
import ru.bell.generatorlease.storage.CarStorage;
import ru.bell.generatorlease.storage.ClientStorage;

import java.util.Random;

@RequiredArgsConstructor
@Slf4j
@Service
public class ContractGenerator {
    private final Sender sender;
    private final CarStorage carStorage;
    private final ClientStorage clientStorage;
    private final int CODE_OK = 200;

    @Scheduled(initialDelay = 2000, fixedRate = 1700)
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
                .setCarVIN((String) carStorage.getNextAndRemove())
                .setClientId((Integer) clientStorage.getNextAndRemove())
                .setDurationInMonths(6)
                .setInitialPayment("0")
                .setInterestRate(new Random().nextInt(15, 35));
        if (sender.doSendContract(contractModel) == CODE_OK) {
            log.info("contract generated {}", contractModel);
        } else {
            log.warn("contract generation failed vin {}: id {}",
                    contractModel.getCarVIN(), contractModel.getClientId());
        }
    }

    private boolean checksOK() {
        return carStorage.hasNext() && clientStorage.hasNext();
    }

    @Scheduled(fixedRate = 5000)
    public void print() {
        System.out.println("===================");
        System.out.println("VIN's IN QUEUE: " + carStorage.getList());
        System.out.println("CLIENTS IN QUEUE: " + clientStorage.getList());
        System.out.println("===================");
    }
}
