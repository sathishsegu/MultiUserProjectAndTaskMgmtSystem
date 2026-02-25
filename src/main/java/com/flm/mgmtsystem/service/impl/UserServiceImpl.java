package com.flm.mgmtsystem.service.impl;

import com.flm.mgmtsystem.repository.UserRepository;
import com.flm.mgmtsystem.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


}
