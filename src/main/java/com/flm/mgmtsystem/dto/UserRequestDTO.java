package com.flm.mgmtsystem.dto;

import com.flm.mgmtsystem.entity.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50)
    private String userName;

    @NotBlank(message = "email is required")
    @Email(message = "Email should be in correct format")
    private String email;

    @NotNull(message = "role is required")
    private Role role;

}
