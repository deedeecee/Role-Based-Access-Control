package com.debankar.rbac_project.security;

import com.debankar.rbac_project.repository.TokenRepository;
import com.debankar.rbac_project.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
 * This component filter is responsible for intercepting requests to validate JWT tokens and set authentication in the
 * security context.
 * OncePerRequestFilter ensures that the filter logic is executed only once per request, even if the filter is mapped
 * multiple times in the filter chain
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // JwtTokenProvider is used for token validation and extraction of user information.
    private final JwtTokenProvider jwtTokenProvider;
    // CustomUserDetailsService is used to load user-specific data during authentication.
    private final CustomUserDetailsService userDetailsService;
    // TokenRepository manages the storage and retrieval of tokens, including their validity status.
    private final TokenRepository tokenRepository;

    public JwtAuthenticationFilter(
            JwtTokenProvider jwtTokenProvider,
            CustomUserDetailsService userDetailsService,
            TokenRepository tokenRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // Extracting the JWT token from the Authorization header of the request
            String token = getTokenFromRequest(request);

            // Ensuring token is neither expired nor revoked from the TokenRepository
            boolean isValidToken = tokenRepository.findByToken(token)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);

            if (token != null && isValidToken && jwtTokenProvider.validateToken(token)) {
                // Extracting the username from the valid JWT token
                String username = jwtTokenProvider.extractUsername(token);
                // Loading user details using the CustomUserDetailsService
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Creating an authentication object with user details and authorities
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,   // Credentials are not needed for JWT-based authentication
                        userDetails.getAuthorities()
                );

                // Storing the authentication object in the SecurityContext for further use in the application
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            // Logging any exceptions that occur during authentication process
            System.err.println("Could not set user authentication: " + e.getMessage());
        }

        // This continues with the next filter in the chain
        filterChain.doFilter(request, response);
    }

    // Extracts the JWT token from the Authorization header of the incoming request.
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if ((bearerToken != null) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);    // Returns the token without "Bearer " prefix
        }
        return null;    // Returns null if no valid bearer token is found
    }
}