package com.flm.mgmtsystem.service;

import com.flm.mgmtsystem.dto.UserRequestDTO;
import com.flm.mgmtsystem.dto.UserResponseDTO;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO dto);
}
