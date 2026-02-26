package com.flm.mgmtsystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReAssignTaskRequestDTO {

    @NotNull(message = "New user ID is required")
    private Long newAssignedUserId;

    @NotNull(message = "Version is required")
    private Long version;
}
