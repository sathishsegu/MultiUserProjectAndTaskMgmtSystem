package com.flm.mgmtsystem.dto;

import com.flm.mgmtsystem.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSemiDetailsDTO {

    private Long userId;
    private String userName;
    private Role role;
}
