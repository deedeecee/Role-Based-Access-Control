package com.debankar.rbac_project.mapper;

import com.debankar.rbac_project.dto.UserCreationDTO;
import com.debankar.rbac_project.dto.UserDTO;
import com.debankar.rbac_project.entity.User;
import com.debankar.rbac_project.enums.Role;
import org.springframework.stereotype.Component;

import java.util.Set;

/*
 * This class is responsible for mapping between User entities and UserDTOs.
 * It provides methods to convert User objects to UserDTOs and vice versa, facilitating data transfer between different
 * layers of the application.
 */
@Component
public class UserMapper {
    // Converts a User entity to a UserDTO.
    public UserDTO toUserDTO(User user) {
        // Extracting fields from the User entity to create a UserDTO.
        Long id = user.getId();
        String username = user.getUsername();
        String email = user.getEmail();
        Set<Role> roles = user.getRoles();

        return new UserDTO(id, username, email, roles);
    }

    // Converts a UserCreationDTO to a User entity.
    public User toUser(UserCreationDTO userDTO) {
        // Constructs and returns a new User entity using the data from the UserCreationDTO.
        return new User(
                userDTO.getUsername(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                userDTO.getRoles()
        );
    }
}