// ignore this file.

package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

public abstract class BaseThread<T> extends Thread {

    private static AtomicLong counter = new AtomicLong(0);

    @JsonIgnore
    protected BlockingQueue<Item<T>> queue;

    private Long id; // no setter
    private AtomicBoolean isActive; // this is reachable by the current thread and the main thread (Shared)
    //private boolean isSender; // I need to differentiate thread type in front-end, Bad design choice I guess // no setter

    public BaseThread(BlockingQueue<Item<T>> queue, Boolean isActive, Integer priority) {
        super();
        this.queue = queue;
        this.id = counter.getAndIncrement();
        this.isActive = new AtomicBoolean(isActive);
        super.setPriority(priority);
    }

    public Long getThreadId(){
        return this.id;
    }

    public Boolean getIsActive(){
        return this.isActive.get();
    }

    public void setIsActive(Boolean active){
        this.isActive.set(active);
    }

    public Integer getThreadPriority(){
        return super.getPriority();
    }

    public void setThreadPriority(Integer priority){
        super.setPriority(priority);
    }

}
