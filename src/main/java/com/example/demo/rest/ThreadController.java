// http://localhost:8080/api-docs works

package com.example.demo.rest;

import com.example.demo.entity.BaseTask;
import com.example.demo.entity.BaseThread;
import com.example.demo.service.ThreadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")  // Allow requests only from localhost:3000
@Tag(name = "Task Management", description = "Endpoints for managing tasks. Important: Tasks are meant to be object that implements runnable. " +
        "In this context, threads and tasks can be used interchangeable.")
public class ThreadController {

    private ThreadService threadService;

    @Autowired
    public ThreadController(ThreadService threadService){
        System.out.println("In constructor: " + getClass().getSimpleName());
        this.threadService = threadService;
    }
    /*
    // form-data
    @PostMapping()
    @Operation(
            summary = "Creates tasks that will be picked up by threads lying in the thread-pool. " +
                        "Extra Info: Thread Pool's task queue is a priority queue based on task's priorities." +
                    "Extra info: Thread pool runs each second.",
            description = "Number of tasks, their priorities and whether they are active or stopped can be specified" +
                        "by parameter passing as form-data. Tasks can be in two forms: Sender and Receiver." +
                    "HTTP status of the response is important, body part is trivial." +
                    "Could have returned the tasks. See GET /threads"
    )
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
    */
    // json
    @PostMapping()
    @Operation(
            summary = "Creates tasks that will be picked up by threads lying in the thread-pool. " +
                    "Extra Info: Thread Pool's task queue is a priority queue based on task's priorities." +
                    "Extra info: Thread pool runs each second.",
            description = "Waits a RequestBody formatted json. {waitingThreads: Collection}" +
                    "Collection element should be in the format {isSender: true/false, isActive: true/false, priority: 5}" +
                    "HTTP status of the response is important, body part is trivial." +
                    "Could have returned the tasks. See GET /threads"
    )
    public ResponseEntity<String> createThreads(@RequestBody Map<String, List<Map<String, Object>>> requestBody){
        //System.out.println(requestBody);
        try {
            this.threadService.createTasks(requestBody.get("waitingThreads"));
            return new ResponseEntity<>("successful", HttpStatus.OK);
        }
        catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @PutMapping("/activity")
    @Operation(
            summary = "Updates the activity of the task determined by its id and returns the updated object.",

            description = "id is used for determining to be started/paused task stored in a hashmap(thread safe)." +
                    "If the request parameter isActive is true, the task will start or continue if it was already live." +
                    "If the parameter is false, logic is otherwise."
    )
    public ResponseEntity<BaseTask<String>> updateThreadActivity(@RequestParam("id") Long threadId,
                                                                 @RequestParam("isActive") Boolean isActive){ // isActive = true -> start
        return new ResponseEntity<>(this.threadService.updateTaskActivity(threadId, isActive), HttpStatus.OK);
    }

    @PutMapping("/priority")
    @Operation(
            summary = "Updates the priority of the task determined by its id and returns the updated object.",

            description = "id is used for determining the specific task whose priority is going to change."

    )
    public ResponseEntity<BaseTask<String>> updateThreadPriority(@RequestParam("id") Long threadId,
                                                                    @RequestParam("priority") int priority){
        return new ResponseEntity<>(this.threadService.updateTaskPriority(threadId, priority), HttpStatus.OK);
    }

    // returning {"queue": Collection(queue)}
    @GetMapping("/resource")
    @Operation(
            summary = "Gets the state of the shared queue resource. Returning as returning {\"queue\": Collection(queue)}",

            description = "Queue is implemented thread-safe"
    )
    public ResponseEntity<Map<String, Object>> getQueue(){
        return new ResponseEntity<>(this.threadService.getQueue(), HttpStatus.OK);
    }

    // returning {"Threads": Collection(Priority Queue)}
    @GetMapping("/threads")
    @Operation(
            summary = "Gets the state of all tasks. Returning as returning {\"Threads\": Collection(hashmap.values())}",

            description = "As described earlier, tasks residen in a hashmap but this endpoint feed each task into a priority queue" +
                    " (based on priority of task) and return this priority queue."
    )
    public ResponseEntity<Map<String, Object>> getThreads(/*@RequestParam("isSenderThread") Boolean isSenderThread*/){
        return new ResponseEntity<>(this.threadService.getTasks(/*isSenderThread*/), HttpStatus.OK);
    }

    @DeleteMapping()
    @Operation(
            summary = "Specified task is removed from the hashmap. From now on, this task can not be fed into thread pool that runs each second.",

            description = "Returns the removed task."
    )
    public ResponseEntity<BaseTask<String>> deleteThread(@RequestParam("id") Long threadId){
        return new ResponseEntity<>(this.threadService.deleteTask(threadId), HttpStatus.OK);
    }
}
