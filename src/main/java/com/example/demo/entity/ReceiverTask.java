package com.example.demo.entity;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ReceiverTask<T> extends BaseTask<T>{

    public ReceiverTask(BlockingQueue<Item<T>> queue, Boolean isActive, Integer priority) {
        super(queue, isActive, priority, false);
    }

    @Override
    public void run() {
        try {
            //while (true) {
                //if(super.getIsActive()){
                    //Item<T> item = super.queue.take(); // this waits // this causes thread pool to get stuck if consumer tasks get the threads when queue size = 0
                    Item<T> item = super.queue.poll(0, TimeUnit.MILLISECONDS);
                    System.out.println("Thread " + super.getThreadId() + " consumed: " + item);
                    //Thread.sleep(1000); // Simulate delay
                //}
            //}
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread " + super.getThreadId() + " interrupted.");
        }
    }

}
