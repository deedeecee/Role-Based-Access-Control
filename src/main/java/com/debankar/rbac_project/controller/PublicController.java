package com.debankar.rbac_project.controller;

import com.debankar.rbac_project.dto.UserCreationDTO;
import com.debankar.rbac_project.dto.UserDTO;
import com.debankar.rbac_project.entity.User;
import com.debankar.rbac_project.mapper.UserMapper;
import com.debankar.rbac_project.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 * This controller handles public endpoints related to user registration and authentication.
 * It is designed to expose functionality that does not require prior authentication.
 */
@RestController
@RequestMapping("/api/v1/public")
public class PublicController {
    // UserService is injected to handle business logic related to user operations.
    private final UserService userService;
    // UserMapper is used to convert between User entities and UserDTOs for data transfer.
    private final UserMapper userMapper;

    public PublicController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    // Endpoint for user registration. Accepts a UserCreationDTO object containing user details.
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserCreationDTO userDTO) {
        /*
         * The registration process involves validating the input data and creating a new user.
         * By using the UserService, we encapsulate the business logic and ensure that any necessary
         * validation or processing (e.g., checking for existing users) occurs within that layer.
         */
        User createdUser = userService.registerUser(userDTO);

        /*
         * After successfully creating the user, we map the User entity to a UserDTO for returning a response.
         * This separation of concerns (entity vs. DTO) helps maintain clean architecture and ensures that only
         * relevant data is exposed to the client.
         */
        return new ResponseEntity<>(userMapper.toUserDTO(createdUser), HttpStatus.CREATED);
    }

    // Endpoint for authenticating users. Accepts email and password as request parameters.
    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestParam String email, @RequestParam String password) {
        /*
         * The authentication process verifies the user's credentials and generates a JWT token if successful.
         * By putting this logic to the UserService, we maintain a clear separation of concerns and keep our
         * controller focused on handling HTTP requests and responses.
         */
        String jwtToken = userService.authenticate(email, password);

        /*
         * On successful authentication, we return the JWT token in the response body.
         * This token will be used by clients for subsequent requests requiring authentication, thus enabling
         * stateless session management in our application.
         */
        return ResponseEntity.ok("Authentication successful for email: " + email + "\nJWT Token: " + jwtToken);
    }
}
