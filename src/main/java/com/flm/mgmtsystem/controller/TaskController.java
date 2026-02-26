package com.flm.mgmtsystem.controller;

import com.flm.mgmtsystem.dto.TaskRequestDTO;
import com.flm.mgmtsystem.dto.TaskResponseDTO;
import com.flm.mgmtsystem.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(
            @RequestHeader("X-USER-ID") Long userId,
            @Valid @RequestBody TaskRequestDTO dto
            ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(userId, dto));
    }
}
