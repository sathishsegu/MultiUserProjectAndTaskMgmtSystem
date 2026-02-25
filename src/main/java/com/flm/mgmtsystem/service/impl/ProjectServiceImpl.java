package com.flm.mgmtsystem.service.impl;

import com.flm.mgmtsystem.repository.ProjectRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl {

    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }
}
