package com.example.demo.rest;

import com.example.demo.entity.BaseTask;
import com.example.demo.entity.BaseThread;
import com.example.demo.service.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")  // Allow requests only from localhost:3000
public class ThreadController {

    private ThreadService threadService;

    @Autowired
    public ThreadController(ThreadService threadService){
        System.out.println("In constructor: " + getClass().getSimpleName());
        this.threadService = threadService;
    }
    // form-data
    @PostMapping()
    public ResponseEntity<String> createThreads(@RequestParam("countSender") Integer countSender,
                                                @RequestParam("prioritySender") Integer prioritySender,
                                                @RequestParam("isActiveSender") Boolean isActiveSender,
                                                @RequestParam("countReceiver") Integer countReceiver,
                                                @RequestParam("priorityReceiver") Integer priorityReceiver,
                                                @RequestParam("isActiveReceiver") Boolean isActiveReceiver){
        this.threadService.createTasks(countSender, prioritySender, isActiveSender, countReceiver, priorityReceiver, isActiveReceiver);
        return new ResponseEntity<>("countSender: " + countSender + " prioritySender: " + prioritySender + " isActiveSender: " + isActiveSender +
                                "\ncountReceiver: " + countReceiver + " priorityReceiver: " + priorityReceiver + " isActiveReceiver: " + isActiveReceiver, HttpStatus.OK);
    }

    @PutMapping("/activity")
    public ResponseEntity<BaseTask<String>> updateThreadActivity(@RequestParam("id") Long threadId,
                                                                 @RequestParam("isActive") Boolean isActive){ // isActive = true -> start
        return new ResponseEntity<>(this.threadService.updateTaskActivity(threadId, isActive), HttpStatus.OK);
    }

    @PutMapping("/priority")
    public ResponseEntity<BaseTask<String>> updateThreadPriority(@RequestParam("id") Long threadId,
                                                                    @RequestParam("priority") int priority){
        return new ResponseEntity<>(this.threadService.updateTaskPriority(threadId, priority), HttpStatus.OK);
    }

    // returning {"queue": Collection(queue)}
    @GetMapping("/resource")
    public ResponseEntity<Map<String, Object>> getQueue(){
        return new ResponseEntity<>(this.threadService.getQueue(), HttpStatus.OK);
    }

    // returning {"Threads": Collection(hashmap)}
    @GetMapping("/threads")
    public ResponseEntity<Map<String, Object>> getThreads(/*@RequestParam("isSenderThread") Boolean isSenderThread*/){
        return new ResponseEntity<>(this.threadService.getTasks(/*isSenderThread*/), HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<BaseTask<String>> deleteThread(@RequestParam("id") Long threadId){
        return new ResponseEntity<>(this.threadService.deleteTask(threadId), HttpStatus.OK);
    }
}
