package com.debankar.rbac_project.service;

import com.debankar.rbac_project.entity.User;
import com.debankar.rbac_project.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/*
 * This service implements Spring Security's UserDetailsService interface.
 * It is responsible for loading user-specific data during authentication based on their email.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Loads user details by email, which acts as the username in this context.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Retrieving a user by email from the repository. If not found, we throw an exception.
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Mapping the user's roles to GrantedAuthority objects, which are used by Spring Security for authorization.
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());

        // Building a UserDetails object that Spring Security uses for authentication.
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())      // Sets the username (email)
                .password(user.getPassword())   // Sets the password (hashed)
                .authorities(authorities)       // Sets the user's authorities (roles)
                .build();
    }
}