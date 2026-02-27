package com.flm.mgmtsystem.service.impl;

import com.flm.mgmtsystem.dto.*;
import com.flm.mgmtsystem.entity.Project;
import com.flm.mgmtsystem.entity.User;
import com.flm.mgmtsystem.entity.enums.Role;
import com.flm.mgmtsystem.exception.*;
import com.flm.mgmtsystem.repository.ProjectRepository;
import com.flm.mgmtsystem.repository.UserRepository;
import com.flm.mgmtsystem.service.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProjectResponseDTO createProject(Long userId, ProjectRequestDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID - " + userId));

        if(user.getRole() != Role.ADMIN) {
            throw new UnAuthorizedActionException("Only ADMIN can create the project");
        }

        Project project = modelMapper.map(dto, Project.class);
        project.setCreatedBy(user);
        Project savedProject = projectRepository.save(project);
        return modelMapper.map(savedProject, ProjectResponseDTO.class);
    }

    @Override
    public AddUsersToProjectResponseDTO addUsersToProject(Long projectId, Long userId, AddUsersToProjectRequestDTO dto) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID - " + projectId));

        if(!project.getIsActive()) {
            throw new InvalidProjectStateException("Cannot add user/s to Inactive projects");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID - " + userId));

        if(user.getRole() != Role.ADMIN && user.getRole() != Role.MANAGER) {
            throw new UnAuthorizedActionException("Developer can't add another developer to the project");
        }

        List<User> usersToAdd = userRepository.findAllById(dto.getUserIds());

        Set<User> existingUsers = new HashSet<>(project.getUsers());

        List<User> newUsers = usersToAdd.stream()
                .filter(u -> !existingUsers.contains(u))
                .toList();

        if(newUsers.isEmpty()) {
            throw new ResourceConflictException("Users already assigned to this project");
        }

        project.getUsers().addAll(newUsers);

        projectRepository.save(project);

        List<UserSemiDetailsDTO> addedUsers = newUsers.stream()
                .map(validUser -> modelMapper.map(validUser, UserSemiDetailsDTO.class))
                .toList();

        AddUsersToProjectResponseDTO response = modelMapper.map(project, AddUsersToProjectResponseDTO.class);
        response.setUsers(addedUsers);

        return response;
    }

    @Override
    public ProjectResponseDTO deactivateProject(Long userId, Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID - " + projectId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID - " + userId));
        if(user.getRole() != Role.ADMIN) {
            throw new UnAuthorizedActionException("Only ADMIN can deactivate the project");
        }
        project.setIsActive(false);
        Project updatedProject = projectRepository.save(project);
        return modelMapper.map(updatedProject, ProjectResponseDTO.class);
    }
}
