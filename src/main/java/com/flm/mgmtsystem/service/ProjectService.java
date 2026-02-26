package com.flm.mgmtsystem.service;

import com.flm.mgmtsystem.dto.AddUsersToProjectRequestDTO;
import com.flm.mgmtsystem.dto.AddUsersToProjectResponseDTO;
import com.flm.mgmtsystem.dto.ProjectRequestDTO;
import com.flm.mgmtsystem.dto.ProjectResponseDTO;

public interface ProjectService {

    ProjectResponseDTO createProject(Long userId, ProjectRequestDTO dto);
    AddUsersToProjectResponseDTO addUsersToProject(Long projectId, Long userId, AddUsersToProjectRequestDTO dto);
    ProjectResponseDTO deactivateProject(Long userId, Long projectId);
}
