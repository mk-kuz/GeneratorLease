package ru.bell.generatorlease.models;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class ContractModel {
    private String carVIN;
    private int clientId;
    private int durationInMonths;
    private String initialPayment;
    private int interestRate;
}
