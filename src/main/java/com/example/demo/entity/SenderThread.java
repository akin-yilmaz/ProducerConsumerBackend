// ignore this file.

package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.function.Supplier;

public class SenderThread<T> extends BaseThread<T>{

    @JsonIgnore
    protected Supplier<Item<T>> factory;

    public SenderThread(BlockingQueue<Item<T>> queue, Supplier<Item<T>> factory, Boolean isActive, Integer priority) {
        super(queue, isActive, priority);
        this.factory = factory;
    }

    @Override
    public void run() {
        //try {
            System.out.println("Sender Thread: " + super.getThreadId() + " is running.");
            //while (true) {
            if(super.getIsActive()){
                Item<T> item = factory.get();  // Create a new instance of Item<T>
                super.queue.offer(item);
                //super.queue.put(item); // it waits if q is full.
                System.out.println("Thread " + super.getThreadId() + " produced: " + item);
                //Thread.sleep(1000); // Simulate delay
            }
            //}
        //}
        /*
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread " + super.getThreadId() + " interrupted.");
        }
        */
    }

}
