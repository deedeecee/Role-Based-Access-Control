package com.debankar.rbac_project.entity.token;

import com.debankar.rbac_project.entity.User;
import com.debankar.rbac_project.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;

/*
 * This class represents the Token entity in the application.
 * It is used to manage JWT tokens associated with users, including their state (valid/expired/revoked).
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id     // Marks this field as the primary key of the entity.
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // Specifies that the primary key should be generated
    private Long id;                                        // automatically by the database.

    private String token;   // The actual JWT token string.

    // The type of token (e.g., BEARER), which can help differentiate between different token types.
    @Enumerated(EnumType.STRING)    // Specifies that the enum value should be stored as a string in the database.
    private TokenType tokenType;

    private boolean expired;        // Indicates whether the token has expired (true if expired).

    private boolean revoked;        // Indicates whether the token has been revoked (true if revoked).

    // Defines a many-to-one relationship with the User entity, where multiple tokens can belong to one user.
    @ManyToOne
    @JoinColumn(name = "user_id")   // Specifies the foreign key column in the Token table that references the User
                                    // table.
    private User user;              // The user associated with this token.
}
