package com.flm.mgmtsystem.controller;

import com.flm.mgmtsystem.dto.ReAssignTaskRequestDTO;
import com.flm.mgmtsystem.dto.TaskRequestDTO;
import com.flm.mgmtsystem.dto.TaskResponseDTO;
import com.flm.mgmtsystem.dto.UpdateTaskStatusRequestDTO;
import com.flm.mgmtsystem.entity.enums.Status;
import com.flm.mgmtsystem.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Tasks",
        description = "APIs for managing the Tasks"
)
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @Operation(
            summary = "Create Task",
            description = "Only Manager can create a task"
    )
    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(
            @RequestHeader("X-USER-ID") Long userId,
            @Valid @RequestBody TaskRequestDTO dto
            ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(userId, dto));
    }


    @Operation(
            summary = "Re assign a Task",
            description = "Only manager can re assign a task"
    )
    @PutMapping("/{taskId}/reassign")
    public ResponseEntity<TaskResponseDTO> reassignTask(
            @PathVariable Long taskId,
            @RequestHeader ("X-USER-ID") Long userId,
            @Valid @RequestBody ReAssignTaskRequestDTO dto
            ) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.reAssignTask(taskId, userId, dto));
    }


    @Operation(
            summary = "Change status of the Task",
            description = "Only developer can change the task status"
    )
    @PutMapping("/{taskId}/status")
    public ResponseEntity<TaskResponseDTO> changeStatus(
            @PathVariable Long taskId,
            @RequestHeader ("X-USER-ID") Long userId,
            @Valid @RequestBody UpdateTaskStatusRequestDTO dto
            ) {
        return ResponseEntity.status(HttpStatus.OK).body(taskService.changeTaskStatus(taskId, userId, dto));
    }


    @Operation(
            summary = "Get Tasks by Project",
            description = "Get Tasks for Active Projects"
    )
    @GetMapping("/project/{projectId}")
    public ResponseEntity<Page<TaskResponseDTO>> getTasksByProject(
            @PathVariable Long projectId,
            @RequestHeader ("X-USER-ID") Long userId,
            @RequestParam (defaultValue = "0") Integer page,
            @RequestParam (defaultValue = "5") Integer size,
            @RequestParam (defaultValue = "taskId") String sortBy,
            @RequestParam (defaultValue = "asc") String sortDir
    ) {
        return ResponseEntity.ok(taskService.getTasksByProject(projectId, userId, page, size, sortBy, sortDir));
    }


    @Operation(
            summary = "Get Tasks by user",
            description = "Get tasks according to their permit"
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<TaskResponseDTO>> getTasksByUser(
            @PathVariable Long userId,
            @RequestHeader ("X-USER-ID") Long requesterId,
            @RequestParam (defaultValue = "0") Integer page,
            @RequestParam (defaultValue = "5") Integer size,
            @RequestParam (defaultValue = "taskId") String sortBy,
            @RequestParam (defaultValue = "asc") String sortDir
    ) {
        return ResponseEntity.ok(taskService.getTasksByUser(userId, requesterId, page, size, sortBy, sortDir));
    }


    @Operation(
            summary = "Get Tasks by Status",
            description = "Get tasks by task status codes"
    )
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<TaskResponseDTO>> getTasksByStatus(
            @PathVariable Status status,
            @RequestHeader ("X-USER-ID") Long userId,
            @RequestParam (defaultValue = "0") Integer page,
            @RequestParam (defaultValue = "5") Integer size,
            @RequestParam (defaultValue = "taskId") String sortBy,
            @RequestParam (defaultValue = "asc") String sortDir
            ) {
        return ResponseEntity.ok(taskService.getTasksByStatus(status, userId, page, size, sortBy, sortDir));
    }
}
