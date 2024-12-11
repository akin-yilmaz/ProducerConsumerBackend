package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public abstract class BaseTask<T> implements Runnable, Comparable<BaseTask<T>>{

    private static AtomicLong counter = new AtomicLong(0);

    @JsonIgnore
    protected BlockingQueue<Item<T>> queue;

    private Long id; // no setter
    private AtomicBoolean isActive;
    //private AtomicInteger priority;
    private Integer priority;
    private Boolean isSender;

    public BaseTask(BlockingQueue<Item<T>> queue, Boolean isActive, Integer priority, Boolean isSender) {
        this.queue = queue;
        this.id = counter.getAndIncrement();
        this.isActive = new AtomicBoolean(isActive);
        //this.priority = new AtomicInteger(priority);
        this.priority = priority;
        this.isSender = isSender;
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
    /*
    public Integer getPriority() {
        return priority.get();
    }

    public void setPriority(Integer priority) {
        this.priority.set(priority);
    }
    */
    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean getIsSender() {return this.isSender;}
    /*
    @Override
    public int compareTo(BaseTask<T> other) {
        return Integer.compare(other.priority.get(), this.priority.get()); // Higher priority comes first
    }
    */
    @Override
    public int compareTo(BaseTask<T> other) {
        return Integer.compare(other.priority, this.priority); // Higher priority comes first
    }

    @Override
    public String toString() {
        return "BaseTask{" +
                "id=" + id +
                ", isActive=" + isActive +
                ", priority=" + priority +
                ", isSender=" + isSender +
                '}';
    }
}
