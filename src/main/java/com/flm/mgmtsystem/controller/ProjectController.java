package com.flm.mgmtsystem.controller;

import com.flm.mgmtsystem.dto.AddUsersToProjectRequestDTO;
import com.flm.mgmtsystem.dto.AddUsersToProjectResponseDTO;
import com.flm.mgmtsystem.dto.ProjectRequestDTO;
import com.flm.mgmtsystem.dto.ProjectResponseDTO;
import com.flm.mgmtsystem.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Projects",
        description = "APIs for managing the Projects"
)
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }


    @Operation(
            summary = "Create Project",
            description = "Creates a project only by ADMIN"
    )
    @PostMapping
    public ResponseEntity<ProjectResponseDTO> createProject(
            @RequestHeader("X-USER-ID") Long userId,
            @Valid @RequestBody ProjectRequestDTO dto
            ) {

        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(userId, dto));
    }


    @Operation(
            summary = "Add Users to the project",
            description = "Add Users to the project only if project is active and only assigned by MANAGER or ADMIN"
    )
    @PostMapping("/{projectId}/users")
    public ResponseEntity<AddUsersToProjectResponseDTO> addUsersToProject(
            @PathVariable Long projectId,
            @RequestHeader("X-USER-ID") Long userId,
            @RequestBody AddUsersToProjectRequestDTO dto
            ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.addUsersToProject(projectId, userId, dto));
    }


    @Operation(
            summary = "Deactivate Project",
            description = "Deactivates Project Only by ADMIN"
    )
    @PutMapping("/{projectId}/deactivate")
    public ResponseEntity<ProjectResponseDTO> deactivateProject(
            @RequestHeader("X-USER-ID") Long userId,
            @PathVariable Long projectId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(projectService.deactivateProject(userId, projectId));
    }
}
