package com.flm.mgmtsystem.service;

import com.flm.mgmtsystem.dto.*;
import com.flm.mgmtsystem.entity.enums.Status;
import org.springframework.data.domain.Page;

public interface TaskService {
    TaskResponseDTO createTask(Long userId, TaskRequestDTO dto);
    TaskResponseDTO reAssignTask(Long taskId, Long userId, ReAssignTaskRequestDTO dto);
    TaskResponseDTO changeTaskStatus(Long taskId, Long userId, UpdateTaskStatusRequestDTO dto);
    Page<TaskResponseDTO> getTasksByProject(Long projectId, Long userId, Integer page, Integer size, String sortBy, String sortDir);
    Page<TaskResponseDTO> getTasksByUser(Long userId, Integer page, Integer size, String sortBy, String sortDir);
    Page<TaskResponseDTO> getTasksByStatus(Status status, Long userId, Integer page, Integer size, String sortBy, String sortDir);
}
