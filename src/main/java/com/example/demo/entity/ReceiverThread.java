// ignore this file.

package com.example.demo.entity;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class ReceiverThread<T> extends BaseThread<T>{

    public ReceiverThread(BlockingQueue<Item<T>> queue, Boolean isActive, Integer priority) {
        super(queue, isActive, priority);
    }

    @Override
    public void run() {
        try {
            System.out.println("Receiver Thread: " + super.getThreadId() + " is running.");
            //while (true) {
            if(super.getIsActive()){

                //Item<T> item = super.queue.take(); // this waits
                Item<T> item = super.queue.poll(0, TimeUnit.MILLISECONDS);
                System.out.println("Thread " + super.getThreadId() + " consumed: " + item);
                //Thread.sleep(1000); // Simulate delay
            }
            //}
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread " + super.getThreadId() + " interrupted.");
        }
    }

}
