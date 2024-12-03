package com.debankar.rbac_project.service;

import com.debankar.rbac_project.dto.UserCreationDTO;
import com.debankar.rbac_project.entity.User;

import java.util.List;

/*
 * This interface defines the contract for user-related operations.
 * It abstracts the business logic related to user management, allowing for different implementations.
 */
public interface UserService {
    /*
     * Registers a new user based on the provided UserCreationDTO.
     * This method is responsible for validating input data and ensuring that business rules are followed.
     */
    User registerUser(UserCreationDTO userDTO);

    /*
     * Authenticates a user based on their email and password.
     * This method should validate credentials and generate a JWT token upon successful authentication.
     */
    String authenticate(String email, String password);

    // Finds a user by their unique identifier (userId).
    User findByUserId(Long userId);

    // Finds a user by their email address.
    User findByEmail(String email);

    // Retrieves all users from the system.
    List<User> findAll();
}
