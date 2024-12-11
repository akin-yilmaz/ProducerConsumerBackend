package com.example.demo.entity;

import java.util.concurrent.atomic.AtomicLong;

public class Item<T> {

    private static AtomicLong counter = new AtomicLong(0);

    private Long id;
    private T value;

    public Item(T value) {
        this.id = counter.getAndIncrement();
        this.value = value;
    }

    public static AtomicLong getIdCounter() {
        return counter;
    }

    public Long getId(){
        return this.id;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}
