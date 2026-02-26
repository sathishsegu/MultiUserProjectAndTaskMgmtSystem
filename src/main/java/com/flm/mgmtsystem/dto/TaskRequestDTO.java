package com.flm.mgmtsystem.dto;

import com.flm.mgmtsystem.entity.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDTO {

    @NotBlank(message = "Task title is required")
    @Size(min = 3, max = 50)
    private String taskTitle;

    @NotBlank(message = "Task Description is required")
    @Size(min = 10, max = 100)
    private String taskDescription;

    @NotNull(message = "Task Priority is Required")
    private Priority priority;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    @NotNull(message = "User ID is required")
    private Long assignedToUserId;
}
