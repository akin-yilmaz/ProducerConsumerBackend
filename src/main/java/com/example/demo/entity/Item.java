package com.example.demo.entity;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.concurrent.atomic.AtomicLong;

@Schema(description = "Represents a Item entity in the system.")
public class Item<T> {

    private static AtomicLong counter = new AtomicLong(0);

    @Schema(description = "Unique identifier for the item. Each instantiation increments an AtomicLong counter.", example = "1")
    private Long id;
    @Schema(description = "Generic T type value", example = "Depends on T")
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
