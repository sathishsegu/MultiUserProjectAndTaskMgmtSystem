package com.flm.mgmtsystem.dto;

import com.flm.mgmtsystem.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private Long userId;
    private String userName;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
}
