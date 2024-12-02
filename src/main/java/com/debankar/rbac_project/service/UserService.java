package com.debankar.rbac_project.service;

import com.debankar.rbac_project.dto.UserCreationDTO;
import com.debankar.rbac_project.entity.User;

import java.util.List;

public interface UserService {
    User registerUser(UserCreationDTO userDTO);
    String authenticate(String email, String password);
    User findByUserId(Long userId);
    User findByEmail(String email);
    List<User> findAll();
}
