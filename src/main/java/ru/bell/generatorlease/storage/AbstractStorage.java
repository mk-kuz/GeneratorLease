package ru.bell.generatorlease.storage;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

@Service
public abstract class AbstractStorage {
    protected final Queue<Object> elements;

    public AbstractStorage() {
        this.elements = new LinkedList<>();
    }

    public Queue<?> getList() {
        return elements;
    }

    public Object getNextAndRemove() throws NoSuchElementException {
        return elements.poll();
    }

    public boolean hasNext() {
        return !elements.isEmpty();
    }

    public void add(Object item) {
        elements.add(item);
    }
}
