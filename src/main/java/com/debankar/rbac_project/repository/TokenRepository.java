package com.debankar.rbac_project.repository;

import com.debankar.rbac_project.entity.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/*
 * This interface is responsible for data access related to Token entities.
 * It extends JpaRepository, providing CRUD operations and additional query methods specific to token management.
 */
public interface TokenRepository extends JpaRepository<Token, Long> {
    /*
     * Custom query to retrieve all valid tokens associated with a user by their user ID.
     * A token is considered valid if it is neither expired nor revoked.
     */
    @Query("""
        SELECT t FROM Token t 
        INNER JOIN User u ON t.user.id = u.id
        WHERE u.id = :#{#userId}
        AND (t.expired = false OR t.revoked = false)
        """)
    List<Token> findAllValidTokensByUserId(@Param("userId") Long userId);

    /*
     * Retrieves a token by its string representation.
     * This method returns an Optional<Token> to handle cases where the token may not exist, allowing for safe handling
     * of null values.
     */
    Optional<Token> findByToken(String token);
}
