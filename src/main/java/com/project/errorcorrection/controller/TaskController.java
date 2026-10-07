package com.project.errorcorrection.controller;

import com.project.errorcorrection.dto.CreateTaskRequest;
import com.project.errorcorrection.dto.CreateTaskResponse;
import com.project.errorcorrection.dto.TaskStatusResponse;
import com.project.errorcorrection.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST-контроллер обработки запросов на создание и получение задач корректировки текста.
 */
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateTaskResponse createTask(@Valid @RequestBody CreateTaskRequest request) {
        return taskService.createTask(request);
    }

    @GetMapping("/{id}")
    public TaskStatusResponse getTask(@PathVariable UUID id) {
        return taskService.getTask(id);
    }
}
