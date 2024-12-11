package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.concurrent.BlockingQueue;
import java.util.function.Supplier;

public class SenderTask<T> extends BaseTask<T>{

    @JsonIgnore
    protected Supplier<Item<T>> factory;

    public SenderTask(BlockingQueue<Item<T>> queue, Supplier<Item<T>> factory, Boolean isActive, Integer priority) {
        super(queue, isActive, priority, true);
        this.factory = factory;
    }

    @Override
    public void run() {
        //try {
            //while (true) {
                //if(super.getIsActive()){
                    Item<T> item = factory.get();  // Create a new instance of Item<T>
                    super.queue.offer(item); // it does not wait if q is full.
                    //super.queue.put(item); // it waits if q is full.
                    System.out.println(super.toString() + " produced: " + item);
                    //Thread.sleep(1000); // Simulate delay
                //}
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
