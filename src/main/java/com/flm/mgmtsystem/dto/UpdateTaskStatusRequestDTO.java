package com.flm.mgmtsystem.dto;

import com.flm.mgmtsystem.entity.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTaskStatusRequestDTO {

    @NotNull(message = "status is required")
    private Status status;

    @NotNull(message = "version is required")
    private Long version;
}
