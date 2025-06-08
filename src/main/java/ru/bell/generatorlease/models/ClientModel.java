package ru.bell.generatorlease.models;


import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ClientModel {
    private String name;
    private String documentNumber;
    private String cellNumber;
}

