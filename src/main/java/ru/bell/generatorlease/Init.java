package ru.bell.generatorlease;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
@Service
@RequiredArgsConstructor
public class Init {
private final     Sender sender;
    @PostConstruct
    public void init() throws URISyntaxException, IOException, InterruptedException {
        log.warn("Init complete");

    }
}
