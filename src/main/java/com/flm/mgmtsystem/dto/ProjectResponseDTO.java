package com.flm.mgmtsystem.dto;

import com.flm.mgmtsystem.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponseDTO {

    private Long projectId;
    private String projectName;
    private String description;
    private LocalDateTime projectCreatedAt;
    private Boolean isActive;
    private UserSemiDetailsDTO createdBy;
}
