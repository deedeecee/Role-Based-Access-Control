package com.debankar.rbac_project.dto;

import com.debankar.rbac_project.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String username;
    private Set<Role> roles;
}
