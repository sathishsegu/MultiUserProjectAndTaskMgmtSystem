package com.flm.mgmtsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddUsersToProjectResponseDTO {

    private Long projectId;
    private String projectName;
    private List<UserSemiDetailsDTO> users;
}
