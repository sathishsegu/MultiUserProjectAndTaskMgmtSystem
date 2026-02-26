package com.flm.mgmtsystem.service;

import com.flm.mgmtsystem.dto.ProjectRequestDTO;
import com.flm.mgmtsystem.dto.ProjectResponseDTO;

public interface ProjectService {

    ProjectResponseDTO createProject(Long userId, ProjectRequestDTO dto);
}
