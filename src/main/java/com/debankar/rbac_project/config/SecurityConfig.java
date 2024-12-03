package com.debankar.rbac_project.config;

import com.debankar.rbac_project.security.JwtAuthenticationFilter;
import com.debankar.rbac_project.service.LogoutService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
 * This class is responsible for configuring security settings for the Spring Boot application.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    // JwtAuthenticationFilter is used to validate JWT tokens for incoming requests.
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    // LogoutService is responsible for handling user logout operations.
    private final LogoutService logoutService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, LogoutService logoutService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.logoutService = logoutService;
    }

    /*
     * Configures the security filter chain that defines how security is applied to HTTP requests.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http    // Authorization rules for different endpoints
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/v1/public/**").permitAll()       // Public endpoints that can be accessed without authentication
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")   // Admin-only endpoints
                        .requestMatchers("/api/v1/moderator/**").hasAnyRole("MODERATOR", "ADMIN")       // Moderator and admin endpoints
                        .requestMatchers("/api/v1/user/**").hasAnyRole("USER", "MODERATOR", "ADMIN")    // User, moderator, and admin endpoints
                        .anyRequest().authenticated()   // All other requests require authentication
                );
        http    // Setting session management to stateless, meaning no session will be created or used
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())   // Disabling CSRF as our services are used by non-browser clients
                .logout(logout -> logout        // Configuring logout behaviour
                        .logoutUrl("/api/v1/public/logout")     // The custom logout url for our application
                        .addLogoutHandler(logoutService)        // Custom logout handler to process logout logic
                        .logoutSuccessHandler(  // Clearing the security context upon successful logout (completely removing a user's authentication information from the system)
                                (request, response, authentication) -> SecurityContextHolder.clearContext()
                        )
                );
        // Disabling frame options to allow embedding in iframes (for viewing H2 console in browser)
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));

        // A JWT token should be validated first, before the system attempts to authenticate using traditional
        // username/password credentials
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        // Retrieving the default AuthenticationManager from Spring Security's configuration
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Using BCrypt hashing algorithm for password encoding
        return new BCryptPasswordEncoder();
    }
}