package com.example.demo.service;

import com.example.demo.entity.BaseTask;
import com.example.demo.entity.BaseThread;

import java.util.Map;

public interface ThreadService<T> {

    void createTasks(Integer countSender, Integer prioritySender, Boolean isActiveSender, Integer countReceiver, Integer priorityReceiver, Boolean isActiveReceiver);

    BaseTask<T> updateTaskActivity(Long threadId, Boolean isActive);

    BaseTask<T> updateTaskPriority(Long threadId, Integer priority);

    Map<String, Object> getQueue();

    Map<String, Object> getTasks(/*Boolean isSenderThread*/);

    BaseTask<String> deleteTask(Long threadId);

}
