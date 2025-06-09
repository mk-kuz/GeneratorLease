package ru.bell.generatorlease.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bell.generatorlease.storage.ClientStorage;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ClientGenerator {
    private final Sender sender;
    private final ClientStorage storage;
    private final int CODE_OK = 200;
    private List<String> names;
    private List<String> documents;
    private List<String> cellphones;
}
