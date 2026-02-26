package com.flm.mgmtsystem.service;

import com.flm.mgmtsystem.dto.ReAssignTaskRequestDTO;
import com.flm.mgmtsystem.dto.TaskRequestDTO;
import com.flm.mgmtsystem.dto.TaskResponseDTO;

public interface TaskService {
    TaskResponseDTO createTask(Long userId, TaskRequestDTO dto);
    TaskResponseDTO reAssignTask(Long taskId, Long userId, ReAssignTaskRequestDTO dto);
}
