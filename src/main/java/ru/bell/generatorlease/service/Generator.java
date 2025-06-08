package ru.bell.generatorlease.service;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class Generator {
    private final Sender sender;
    private final String PREFIX = "123456789000";
    private final char[] CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private final int sizeC = CHARS.length;
    private Map<String, Integer> pointers;
    private List<Map<String, String>> brandsAndTypes;

    @PostConstruct
    public void init() throws InterruptedException {
        constructMaps();
    }

    @Scheduled(fixedRate = 100)
    public void run() throws InterruptedException {
        Map.Entry<String, String> entry = generateBrandAndType()
                .entrySet().stream()
                .findFirst()
                .get();
        sender.doSendCar(
                generateVIN(),
                entry.getKey(),
                entry.getValue(),
                generateYear(),
                generatePrice()
        );
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

    public String generateVIN() throws InterruptedException {
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
        pointers.put("1", 0);
        pointers.put("2", 5);
        pointers.put("3", 10);
        pointers.put("4", 15);
        pointers.put("5", 24);

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
