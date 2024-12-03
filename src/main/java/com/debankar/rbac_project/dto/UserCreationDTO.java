package com.debankar.rbac_project.dto;

import com.debankar.rbac_project.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/*
 * This class serves as a Data Transfer Object (DTO) for user creation requests.
 * It encapsulates the data needed to create a new user and includes validation rules.
 */
@Getter
@Setter
public class UserCreationDTO {
    // Validation annotations to ensure the username is not blank and enforce username length constraints.
    @NotBlank(message = "Username is required")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    private String username;

    // Validation annotations to ensure the email is not blank and email format is valid.
    @NotBlank
    @Email
    private String email;

    // Validation annotations to ensure the password is not blank and enforce minimum password length.
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    // A set of roles assigned to the user being created. This is optional as default role is assigned later.
    private Set<Role> roles;
}
