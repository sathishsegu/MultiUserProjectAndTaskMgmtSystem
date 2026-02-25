package com.flm.mgmtsystem.service.impl;

import com.flm.mgmtsystem.dto.UserRequestDTO;
import com.flm.mgmtsystem.dto.UserResponseDTO;
import com.flm.mgmtsystem.entity.User;
import com.flm.mgmtsystem.exception.ResourceConflictException;
import com.flm.mgmtsystem.repository.UserRepository;
import com.flm.mgmtsystem.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
        if(userRepository.existsByEmail(dto.getEmail())){
            throw new ResourceConflictException("Email already exists");
        }
        User user = modelMapper.map(dto, User.class);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserResponseDTO.class);
    }
}
