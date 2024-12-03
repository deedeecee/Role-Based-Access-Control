package com.debankar.rbac_project.entity;

import com.debankar.rbac_project.enums.Role;
import com.debankar.rbac_project.entity.token.Token;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * This class represents the User entity in the application.
 * It is mapped to the "users" table in the database and contains user-related information.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id     // Marks this field as the primary key of the entity.
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // Specifies that the primary key should be generated
                                                            // automatically by the database.
    private Long id;

    private String username;

    private String email;

    private String password;

    // Indicates that this field is a collection of elements (roles) that are stored in a separate table.
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    // Defines a one-to-many relationship with the Token entity, where each user can have multiple tokens.
    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    // Constructor for creating a User with specified username, email, password, and roles.
    public User(String username, String email, String password, Set<Role> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }
}
