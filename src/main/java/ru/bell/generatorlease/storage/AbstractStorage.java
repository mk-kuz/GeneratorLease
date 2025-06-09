package ru.bell.generatorlease.storage;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Queue;

@Service
public abstract class AbstractStorage {
    protected final Queue<String> elements;

    public AbstractStorage() {
        this.elements = new LinkedList<>();
    }

    public Queue<String> getList() {
        return elements;
    }

    public Optional<String> getNextAndRemove() throws NoSuchElementException {
        return Optional.ofNullable(elements.poll());
    }

    public void add(String item) {
        elements.add(item);
    }
}
