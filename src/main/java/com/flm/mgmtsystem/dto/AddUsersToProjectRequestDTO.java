package com.flm.mgmtsystem.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddUsersToProjectRequestDTO {

    @NotEmpty(message = "User ID list cannot be empty")
    private List<Long> userIds;
}
