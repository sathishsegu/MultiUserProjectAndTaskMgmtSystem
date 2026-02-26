package com.flm.mgmtsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequestDTO {

    @NotBlank(message = "Project Name is Required")
    @Size(min = 3, max = 50, message = "Name size should between 3 and 50 characters")
    private String projectName;

    @NotBlank(message = "Project description is required")
    @Size(min = 5, max = 100)
    private String description;

}
