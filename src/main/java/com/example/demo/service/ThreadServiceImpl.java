package com.example.demo.service;

import com.example.demo.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Supplier;

@Service
public class ThreadServiceImpl implements ThreadService{

    private BlockingQueue<Item<String>> queue; // shared resource by custom created threads
    private Map<Long, BaseTask<String>> taskMap;
    //private Map<Long, BaseTask> senderTaskMap; // Shared resource by each HTTP request
    //private Map<Long, BaseTask> receiverTaskMap; // Shared resource by each HTTP request
    //private final ScheduledExecutorService scheduler; // thread safe
    private final ThreadPoolExecutor executor; // thread safe

    //@Value("${task.scheduled.rate}") // Inject the delay time from application.properties
    //private long delayTime;

    @Autowired
    public ThreadServiceImpl(@Qualifier("priorityExecutor") ThreadPoolExecutor executor) { // inject the executor

        this.queue = new LinkedBlockingQueue<>();
        this.taskMap = new ConcurrentHashMap<>(); // thread safe
        this.executor = executor;
    }

    //@Scheduled(fixedRate = 1000) // 1000 ms = 1 second
    @Scheduled(fixedRateString = "${task.scheduled.rate}") // 1000 ms = 1 second
    public void submitTask() {

        System.out.println("----- 1 second has passed. ------");

        try {

            System.out.println(this.executor);
            for(BaseTask task : this.taskMap.values()){
                if(task.getIsActive()){
                    //executor.submit(task); // This was causing error, IDK why
                    this.executor.execute(task);
                }
            }



        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void createTasks(Integer countSender, Integer prioritySender, Boolean isActiveSender, Integer countReceiver, Integer priorityReceiver, Boolean isActiveReceiver) {

        try {
            Supplier<Item<String>> supplier = () -> new Item<>("Item");

            BaseTask<String> newTask;
            //BaseThread<String> newThread;

            for(int i = 0; i < countSender; i++){

                newTask = new SenderTask<>(this.queue, supplier, isActiveSender, prioritySender);
                this.taskMap.put(newTask.getThreadId(), newTask);

                //this.scheduler.scheduleWithFixedDelay(newThread, 0, delayTime, TimeUnit.MILLISECONDS);
                //this.executor.execute(newTask);
            }

            for(int i = 0; i < countReceiver; i++){

                newTask = new ReceiverTask<>(this.queue, isActiveReceiver, priorityReceiver);
                this.taskMap.put(newTask.getThreadId(), newTask);

                //this.scheduler.scheduleWithFixedDelay(newThread, 0, delayTime, TimeUnit.MILLISECONDS);
                //this.executor.execute(newTask);
            }

        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public BaseTask<String> updateTaskActivity(Long threadId, Boolean isActive) {
        try {
            BaseTask<String> baseTask = this.taskMap.get(threadId);
            baseTask.setIsActive(isActive);
            /*
            if(isActive){ // if started
                this.executor.execute(baseTask);
            }
            else{ // if stopped
                this.executor.remove(baseTask);
            }
            */
            return baseTask;
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public BaseTask<String> updateTaskPriority(Long threadId, Integer priority) {
        try {
            BaseTask<String> baseTask = this.taskMap.get(threadId);
            baseTask.setPriority(priority);
            /*
            if(baseTask.getIsActive()){
                boolean isRemoved = this.executor.remove(baseTask);
                if(isRemoved){
                    this.executor.execute(baseTask);
                }
            }
            */
            return baseTask;
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getQueue() {
        Integer size = this.queue.size();
        return Map.of("queue", this.queue);
    }

    @Override
    public Map<String, Object> getTasks() {
        PriorityBlockingQueue<BaseTask<String>> pBQ = new PriorityBlockingQueue<>();
        pBQ.addAll(this.taskMap.values()); // this does not work as intended
        System.out.println("pBQ: " + pBQ);
        return Map.of("Threads", pBQ);
    }

    @Override
    public BaseTask<String> deleteTask(Long threadId) {
        try {
            BaseTask<String> baseTask = this.taskMap.get(threadId);
            if(baseTask != null){
                this.executor.remove(baseTask);
            }
            return this.taskMap.remove(threadId);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }


}
