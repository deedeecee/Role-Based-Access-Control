package com.debankar.rbac_project.repository;

import com.debankar.rbac_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/*
 * This interface is responsible for data access related to User entities.
 * It extends JpaRepository, providing CRUD operations and additional query methods.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /*
     * Retrieves a user by their email address.
     * This method returns an Optional<User> to handle cases where the user may not exist, allowing for safe handling
     * of null values.
     */
    Optional<User> findByEmail(String email);

    /*
     * Checks if a user with the specified email already exists in the database.
     * This method is useful for validation during user registration to prevent duplicate emails.
     */
    boolean existsByEmail(String email);
}
