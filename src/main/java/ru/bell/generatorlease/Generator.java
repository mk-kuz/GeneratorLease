package ru.bell.generatorlease;


import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
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

    @Scheduled(fixedRate = 200)
    public void run() throws InterruptedException {
        int counter = 0;
        while (counter < 150) {
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
            counter++;
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

    private void testIterate() throws InterruptedException {
        for (int i = 1; i < 6; i++) {
            Integer pointer = pointers.get(String.valueOf(i));
            int bound = sizeC - 1;
            int lag = new Random().nextInt(1, 5);
            int stepInto = pointer + lag;
            Numpad numpad = new Numpad().setPointer(pointer).setBound(bound).setLag(lag).setStepInto(stepInto);
            System.out.println(numpad.toString());
            if (stepInto > bound) {
                System.out.println("ENGAGEMENT");
                System.out.println("curr pointer: " + pointer);
                pointer = stepInto - sizeC;
                Thread.sleep(300);
                System.out.println("new pointer " + pointer);
                System.out.println("lag " + lag);
                System.out.println("stepInto " + stepInto);
                pointers.replace(String.valueOf(i), pointer);
            } else {
                pointer = pointer + lag;
                pointers.replace(String.valueOf(i), pointer);
            }
            Thread.sleep(300);
            System.out.println("new pointer: " + pointer);
        }

    }

    @Accessors(chain = true)
    @Data
    private class Numpad {
        public int pointer;
        public int bound;
        public int lag;
        public int stepInto;

    }

    private void test() throws InterruptedException {
        int i = 0;
        while (true) {
            System.out.println(CHARS[i]);
            if (i == sizeC - 1) {
                Thread.sleep(1500);
                System.out.println("THIS IS THE END");
                i = 0;
            } else {
                i++;
            }
            Thread.sleep(300);
        }
    }

    private void findDuplicates(List<String> vins) {
        int size = vins.size();
        System.out.println("List: " + size);
        Set<String> duplicates = new HashSet<>(vins);
        int setSize = duplicates.size();
        System.out.println("Set: " + setSize);
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
