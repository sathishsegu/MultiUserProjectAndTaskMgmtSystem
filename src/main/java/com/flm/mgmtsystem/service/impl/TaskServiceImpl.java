package com.flm.mgmtsystem.service.impl;

import com.flm.mgmtsystem.repository.TaskRepository;
import com.flm.mgmtsystem.service.TaskService;

public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
}
