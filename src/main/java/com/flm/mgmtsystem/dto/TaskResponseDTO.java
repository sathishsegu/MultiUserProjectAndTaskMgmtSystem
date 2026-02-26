package com.flm.mgmtsystem.dto;

import com.flm.mgmtsystem.entity.enums.Priority;
import com.flm.mgmtsystem.entity.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDTO {

    private Long taskId;
    private String taskTitle;
    private String taskDescription;
    private Status status;
    private Priority priority;
    private UserSemiDetailsDTO assignedTo;
    private ProjectSemiDetailsDTO project;
    private LocalDateTime taskCreatedAt;
    private LocalDateTime updatedAt;
    private Long version;
}
