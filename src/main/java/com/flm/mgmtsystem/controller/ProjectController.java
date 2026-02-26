package com.flm.mgmtsystem.controller;

import com.flm.mgmtsystem.dto.AddUsersToProjectRequestDTO;
import com.flm.mgmtsystem.dto.AddUsersToProjectResponseDTO;
import com.flm.mgmtsystem.dto.ProjectRequestDTO;
import com.flm.mgmtsystem.dto.ProjectResponseDTO;
import com.flm.mgmtsystem.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(
            @RequestHeader("X-USER-ID") Long userId,
            @Valid @RequestBody ProjectRequestDTO dto
            ) {

        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(userId, dto));
    }

    @PostMapping("/{projectId}/users")
    public ResponseEntity<AddUsersToProjectResponseDTO> addUsersToProject(
            @PathVariable Long projectId,
            @RequestHeader("X-USER-ID") Long userId,
            @RequestBody AddUsersToProjectRequestDTO dto
            ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.addUsersToProject(projectId, userId, dto));
    }

    @PutMapping("/{projectId}/deactivate")
    public ResponseEntity<ProjectResponseDTO> deactivateProject(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long projectId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.deactivateProject(userId, projectId));
    }
}
