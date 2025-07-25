package ru.bell.generatorlease.service;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.bell.generatorlease.models.CarModel;
import ru.bell.generatorlease.storage.CarStorage;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarGenerator {
    private final Sender sender;
    private final CarStorage storage;
    private final String PREFIX = "123456789000";
    private final char[] CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private final int CODE_OK = 200;
    private final int sizeC = CHARS.length;
    private Map<String, Integer> pointers;
    private List<Map<String, String>> brandsAndTypes;

    @PostConstruct
    public void init() {
        constructMaps();
    }

    @Scheduled(initialDelay = 1000, fixedRate = 1500)
    public void generateAndSend() {
        Map.Entry<String, String> entry = generateBrandAndType()
                .entrySet().stream()
                .findFirst()
                .get();
        CarModel carModel = new CarModel()
                .setVIN(generateVIN())
                .setBrand(entry.getKey())
                .setType(entry.getValue())
                .setYear(generateYear())
                .setPrice(generatePrice());
        if (sender.doSendCar(carModel) == CODE_OK) {
            storage.add(carModel.getVIN());
        }
    }

    private String generatePrice() {
        int price = new Random().nextInt(100000, 500000);
        return String.valueOf(price);
    }

    private int generateYear() {
        return new Random().nextInt(2000, 2026);
    }

    private Map<String, String> generateBrandAndType() {
        int pointer = new Random().nextInt(0, 8);
        return brandsAndTypes.get(pointer);
    }

    public String generateVIN() {
        StringBuilder vin = new StringBuilder();
        vin.append(PREFIX);
        for (int i = 1; i < 6; i++) {
            Integer pointer = pointers.get(String.valueOf(i));
            vin.append(CHARS[pointer]);
            int bound = sizeC - 1;
            int lag = new Random().nextInt(1, 5);
            int stepInto = pointer + lag;
            if (stepInto > bound) {
                pointer = stepInto - sizeC;
                pointers.replace(String.valueOf(i), pointer);
            } else {
                pointer = pointer + lag;
                pointers.replace(String.valueOf(i), pointer);
            }
        }
        return vin.toString();
    }

    private void constructMaps() {
        pointers = new LinkedHashMap<>();
        pointers.put("1", new Random().nextInt(0, 5));
        pointers.put("2", new Random().nextInt(5, 10));
        pointers.put("3", new Random().nextInt(10, 15));
        pointers.put("4", new Random().nextInt(15, 20));
        pointers.put("5", new Random().nextInt(20, 25));

        List<Map<String, String>> brands = new ArrayList<>();

        brands.add(generateMapPair("BMW", "X5"));
        brands.add(generateMapPair("BMW", "X6"));
        brands.add(generateMapPair("BMW", "X3"));
        brands.add(generateMapPair("Toyota", "Camry"));
        brands.add(generateMapPair("Toyota", "Corolla"));
        brands.add(generateMapPair("Audi", "A4"));
        brands.add(generateMapPair("Audi", "A6"));
        brands.add(generateMapPair("Audi", "A8"));
        brandsAndTypes = brands;

    }

    private Map<String, String> generateMapPair(String brand, String type) {
        Map<String, String> map = new HashMap<>();
        map.put(brand, type);
        return map;
    }
}
