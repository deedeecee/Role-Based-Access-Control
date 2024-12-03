package com.debankar.rbac_project.service;

import com.debankar.rbac_project.repository.TokenRepository;
import com.debankar.rbac_project.entity.token.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

/*
 * This service handles logout operations, including invalidating JWT tokens.
 * It implements Spring Security's LogoutHandler interface to provide custom logout logic.
 */
@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // Retrieving the Authorization header from the request to extract the bearer token.
        String bearerToken = request.getHeader("Authorization");

        // Checking if the bearer token is present and properly formatted.
        if ((bearerToken == null) || !bearerToken.startsWith("Bearer ")) {
            return;
        }

        String token = bearerToken.substring(7);    // Extracting the actual token string from the bearer token.
        Optional<Token> optionalToken = tokenRepository.findByToken(token); // Retrieving the token from repository.

        if (optionalToken.isPresent()) {    // Checking if the token exists.
            Token storedToken = optionalToken.get();

            storedToken.setExpired(true);       // Marks the token as expired.
            storedToken.setRevoked(true);       // Marks the token as revoked to prevent further use.

            tokenRepository.save(storedToken);  // Saves the updated token state back to the repository.
        }
    }
}
