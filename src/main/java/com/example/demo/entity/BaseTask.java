package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Schema(description = "Represents a task(abstract) entity in the system.")
public abstract class BaseTask<T> implements Runnable, Comparable<BaseTask<T>>{

    private static AtomicLong counter = new AtomicLong(0);

    @JsonIgnore
    protected BlockingQueue<Item<T>> queue;
    @Schema(description = "Unique identifier for the item. Each instantiation increments an AtomicLong counter.", example = "1")
    private Long id; // no setter
    @Schema(description = "State indicator of a task whether it is active and running or paused. Paused tasks is not picked by thread pool. " +
            "Its type could have been Boolean instead of AtomicBoolean.", example = "true")
    private AtomicBoolean isActive;
    //private AtomicInteger priority;
    @Schema(description = "Priority of a task. Higher priority tasks are picked first by thread pool", example = "1")
    private Integer priority;
    @Schema(description = "Used for determining whether the task is Sender task or Receiver Task. If true, it means it is a Sender task.", example = "true")
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
