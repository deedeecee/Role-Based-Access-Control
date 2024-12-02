package com.debankar.rbac_project.mapper;

import com.debankar.rbac_project.dto.UserCreationDTO;
import com.debankar.rbac_project.dto.UserDTO;
import com.debankar.rbac_project.entity.User;
import com.debankar.rbac_project.enums.Role;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserMapper {
    public UserDTO toUserDTO(User user) {
        Long id = user.getId();
        String username = user.getUsername();
        String email = user.getEmail();
        Set<Role> roles = user.getRoles();

        return new UserDTO(id, username, email, roles);
    }

    public User toUser(UserCreationDTO userDTO) {
        return new User(
                userDTO.getUsername(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                userDTO.getRoles()
        );
    }
}