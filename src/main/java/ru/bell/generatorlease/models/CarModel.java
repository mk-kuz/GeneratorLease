package ru.bell.generatorlease.models;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class CarModel {
    public String VIN;
    public String brand;
    public String type;
    public int year;
    public String price;
}
