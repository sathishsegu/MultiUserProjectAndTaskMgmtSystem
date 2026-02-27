package com.flm.mgmtsystem.service.impl;

import com.flm.mgmtsystem.dto.*;
import com.flm.mgmtsystem.entity.Project;
import com.flm.mgmtsystem.entity.Task;
import com.flm.mgmtsystem.entity.User;
import com.flm.mgmtsystem.entity.enums.Role;
import com.flm.mgmtsystem.entity.enums.Status;
import com.flm.mgmtsystem.exception.*;
import com.flm.mgmtsystem.repository.ProjectRepository;
import com.flm.mgmtsystem.repository.TaskRepository;
import com.flm.mgmtsystem.repository.UserRepository;
import com.flm.mgmtsystem.service.TaskService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        if(creator.getRole() != Role.MANAGER) {
            throw new UnAuthorizedActionException("Only MANAGER can create a task");
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

    private void validateTransition(Status current, Status newStatus) {
        switch (current) {
            case TODO :
                if(newStatus != Status.IN_PROGRESS) {
                    throw new InvalidTaskTransitionException("TODO can move only to IN_PROGRESS");
                }
                break;

            case IN_PROGRESS :
                if(newStatus != Status.BLOCKED && newStatus != Status.DONE) {
                    throw new InvalidTaskTransitionException("IN_PROGRESS can move only to BLOCKED or DONE");
                }
                break;

            case BLOCKED :
                if(newStatus != Status.IN_PROGRESS) {
                    throw new InvalidTaskTransitionException("BLOCKED can move only to IN_PROGRESS");
                }
                break;

            case DONE :
                throw new InvalidTaskTransitionException("DONE cannot be changed");
        }
    }

    @Override
    public TaskResponseDTO changeTaskStatus(Long taskId, Long userId, UpdateTaskStatusRequestDTO dto) {
       User user = userRepository.findById(userId)
               .orElseThrow(() -> new ResourceNotFoundException("User not found with ID - " + userId));

       Task task = taskRepository.findById(taskId)
               .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID - " + taskId));

       if(!task.getProject().getIsActive()) {
           throw new InvalidProjectStateException("Cannot update task in inactive project");
       }

       if(user.getRole() == Role.DEVELOPER) {
           if(!task.getAssignedTo().getUserId().equals(userId)) {
               throw new UnAuthorizedActionException("You can update only your assigned tasks");
           }
       } else if (user.getRole() == Role.ADMIN || user.getRole() == Role.MANAGER) {
           throw new UnAuthorizedActionException("You ar not allowed to change the status of the task");
       }

       if(!task.getVersion().equals(dto.getVersion())) {
           throw new ObjectOptimisticLockingFailureException(Task.class, taskId);
       }

       validateTransition(task.getStatus(), dto.getStatus());
       task.setStatus(dto.getStatus());
       Task updatedTask = taskRepository.save(task);
       return modelMapper.map(updatedTask, TaskResponseDTO.class);
    }

    @Override
    public Page<TaskResponseDTO> getTasksByProject(Long projectId, Long userId, Integer page, Integer size, String sortBy, String sortDir) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID - " + userId));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID - " + projectId));

        if(!project.getIsActive()) {
            throw new InvalidProjectStateException("Project is Inactive");
        }

        if(!project.getUsers().contains(user) && user.getRole() != Role.ADMIN && user.getRole() != Role.MANAGER) {
            throw new UnAuthorizedActionException("User does not have access to this project");
        }

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Task> taskPage = taskRepository.findByProject(project, pageable);

        return taskPage.map(task -> modelMapper.map(task, TaskResponseDTO.class));
    }

    @Override
    public Page<TaskResponseDTO> getTasksByUser(Long userId, Long requesterId, Integer page, Integer size, String sortBy, String sortDir) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID - " + userId));

        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new ResourceNotFoundException("Requester not found with ID - " + requesterId));

        if(requester.getRole() != Role.ADMIN && requester.getRole() != Role.MANAGER && !requester.getUserId().equals(userId)) {
            throw new UnAuthorizedActionException("You are not allowed to view the user's tasks");
        }

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Task> taskPage = taskRepository.findByAssignedTo(user, pageable);

        return taskPage.map(task -> modelMapper.map(task, TaskResponseDTO.class));
    }

    @Override
    public Page<TaskResponseDTO> getTasksByStatus(Status status, Long userId, Integer page, Integer size, String sortBy, String sortDir) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID - " + userId));

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Task> taskPage;

        if(user.getRole() == Role.ADMIN) {
            taskPage = taskRepository.findByStatus(status, pageable);
        } else if (user.getRole() == Role.MANAGER) {
            taskPage = taskRepository.findByStatusAndProjectIn(status, user.getProjects(), pageable);
        } else {
            taskPage = taskRepository.findByStatusAndAssignedTo(status, user, pageable);
        }

        return taskPage.map(task -> modelMapper.map(task, TaskResponseDTO.class));
    }
}
