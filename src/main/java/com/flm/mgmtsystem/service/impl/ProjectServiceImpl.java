package com.flm.mgmtsystem.service.impl;

import com.flm.mgmtsystem.dto.ProjectRequestDTO;
import com.flm.mgmtsystem.dto.ProjectResponseDTO;
import com.flm.mgmtsystem.entity.Project;
import com.flm.mgmtsystem.entity.User;
import com.flm.mgmtsystem.entity.enums.Role;
import com.flm.mgmtsystem.exception.ResourceNotFoundException;
import com.flm.mgmtsystem.exception.UnAuthorizedActionException;
import com.flm.mgmtsystem.repository.ProjectRepository;
import com.flm.mgmtsystem.repository.UserRepository;
import com.flm.mgmtsystem.service.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
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
}
