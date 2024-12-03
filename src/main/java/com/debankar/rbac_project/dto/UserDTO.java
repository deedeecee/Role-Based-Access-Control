package com.debankar.rbac_project.dto;

import com.debankar.rbac_project.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/*
 * This class serves as a Data Transfer Object (DTO) for transferring user data.
 * It encapsulates the data that should be returned in user-related responses.
 */
@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private Long id;            // The unique identifier of the user.
    private String username;    // The username of the user.
    private String email;       // The email address of the user.

    // A set of roles assigned to the user. This provides information about the user's permissions and access levels.
    private Set<Role> roles;
}
