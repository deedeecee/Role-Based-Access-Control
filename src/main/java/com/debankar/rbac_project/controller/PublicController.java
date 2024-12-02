package com.debankar.rbac_project.controller;

import com.debankar.rbac_project.dto.UserCreationDTO;
import com.debankar.rbac_project.dto.UserDTO;
import com.debankar.rbac_project.entity.User;
import com.debankar.rbac_project.mapper.UserMapper;
import com.debankar.rbac_project.security.JwtTokenProvider;
import com.debankar.rbac_project.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/public")
public class PublicController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;

    public PublicController(UserService userService, UserMapper userMapper, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserCreationDTO userDTO) {
        User createdUser = userService.registerUser(userDTO);
        return new ResponseEntity<>(userMapper.toUserDTO(createdUser), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestParam String email, @RequestParam String password) {
        String jwtToken = userService.authenticate(email, password);
        return ResponseEntity.ok("Authentication successful for email: " + email + "\nJWT Token: " + jwtToken);
    }
}
