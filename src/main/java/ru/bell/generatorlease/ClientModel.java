package ru.bell.generatorlease;


import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ClientModel {

    private String name;

    private String documentNumber;

    private String cellNumber;
}

