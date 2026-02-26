package com.flm.mgmtsystem.service.impl;

import com.flm.mgmtsystem.dto.ReAssignTaskRequestDTO;
import com.flm.mgmtsystem.dto.TaskRequestDTO;
import com.flm.mgmtsystem.dto.TaskResponseDTO;
import com.flm.mgmtsystem.entity.Project;
import com.flm.mgmtsystem.entity.Task;
import com.flm.mgmtsystem.entity.User;
import com.flm.mgmtsystem.entity.enums.Role;
import com.flm.mgmtsystem.exception.InvalidAssignmentException;
import com.flm.mgmtsystem.exception.InvalidProjectStateException;
import com.flm.mgmtsystem.exception.ResourceNotFoundException;
import com.flm.mgmtsystem.exception.UnAuthorizedActionException;
import com.flm.mgmtsystem.repository.ProjectRepository;
import com.flm.mgmtsystem.repository.TaskRepository;
import com.flm.mgmtsystem.repository.UserRepository;
import com.flm.mgmtsystem.service.TaskService;
import jakarta.persistence.OptimisticLockException;
import org.modelmapper.ModelMapper;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ProjectRepository projectRepository;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, ModelMapper modelMapper, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.projectRepository = projectRepository;
    }

    @Override
    public TaskResponseDTO createTask(Long userId, TaskRequestDTO dto) {

        User creator = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID - " + userId));
        if(creator.getRole() != Role.MANAGER && creator.getRole() != Role.ADMIN) {
            throw new UnAuthorizedActionException("Developer can't assign a task");
        }

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID - " + dto.getProjectId()));
        if(!project.getIsActive()) {
            throw new InvalidProjectStateException("Cannot create task for Inactive project");
        }

        User assignedUser = userRepository.findById(dto.getAssignedToUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Assigned User not found with ID - " + dto.getAssignedToUserId()));

        Set<User> projectUsers = new HashSet<>(project.getUsers());

        if(!projectUsers.contains(assignedUser)) {
            throw new UnAuthorizedActionException("User does not belong to this project");
        }

        if(assignedUser.getRole() != Role.DEVELOPER) {
            throw new InvalidAssignmentException("Task can be assigned only to Developers");
        }

        Task task = modelMapper.map(dto, Task.class);
        task.setProject(project);
        task.setAssignedTo(assignedUser);
        Task savedTask = taskRepository.save(task);
        return modelMapper.map(savedTask, TaskResponseDTO.class);
    }

    @Override
    public TaskResponseDTO reAssignTask(Long taskId, Long userId, ReAssignTaskRequestDTO dto) {
        User manager = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Manager not found with ID - " + userId));
        if(manager.getRole() != Role.MANAGER) {
            throw new UnAuthorizedActionException("Only MANAGER can re assign a task");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID - " + taskId));

        if (!task.getVersion().equals(dto.getVersion())) {
            throw new ObjectOptimisticLockingFailureException(Task.class, taskId);
        }

        Project project = task.getProject();

        if (!project.getIsActive()) {
            throw new InvalidProjectStateException("Cannot reassign task in inactive project");
        }

        User newUser = userRepository.findById(dto.getNewAssignedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID - " + dto.getNewAssignedUserId()));

        Set<User> projectUsers = new HashSet<>(project.getUsers());

        if(!projectUsers.contains(newUser)) {
            throw new InvalidAssignmentException("User not assigned to this project");

        }

        if(newUser.getRole() != Role.DEVELOPER) {
            throw new InvalidAssignmentException("Task can be only assigned to the Developers");
        }

        task.setAssignedTo(newUser);
        Task updatedTask = taskRepository.save(task);
        return modelMapper.map(updatedTask, TaskResponseDTO.class);
    }
}
