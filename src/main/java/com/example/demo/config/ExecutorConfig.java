package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ExecutorConfig {

    @Bean(name = "priorityExecutor")
    public ThreadPoolExecutor priorityExecutor() {
        // PriorityBlockingQueue for task queue
        PriorityBlockingQueue<Runnable> priorityQueue = new PriorityBlockingQueue<>();

        // Custom ThreadPoolExecutor
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                4, // Core pool size
                8, // Maximum pool size
                60, // Keep-alive time
                TimeUnit.SECONDS,
                priorityQueue // Use priority queue
        );

        return executor;
    }

}
