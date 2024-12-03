package com.debankar.rbac_project.service;

import com.debankar.rbac_project.entity.token.Token;
import com.debankar.rbac_project.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * This class contains unit tests for the LogoutService class.
 */
public class LogoutServiceTest {

    @InjectMocks    // Creates an instance of LogoutService and injects mocked dependencies into it.
    private LogoutService logoutService;

    @Mock   // Creates a mock instance of TokenRepository for testing.
    private TokenRepository tokenRepository;

    @Mock   // Creates a mock instance of HttpServletRequest for testing.
    private HttpServletRequest request;

    @Mock   // Creates a mock instance of HttpServletResponse for testing.
    private HttpServletResponse response;

    @Mock   // Creates a mock instance of Authentication for testing.
    private Authentication authentication;

    private final String validToken = "validToken";
    private Token tokenEntity;

    /** Setting up the necessary context before each test case. */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        tokenEntity = new Token();
        tokenEntity.setToken(validToken);
        tokenEntity.setExpired(false);
        tokenEntity.setRevoked(false);
    }

    /**
     * Test case to verify successful logout functionality.
     * This test checks that when a valid Bearer token is provided,
     * the service correctly marks the token as expired and revoked,
     * and that it saves these changes to the repository.
     */
    @Test
    public void logout_Success() {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(tokenRepository.findByToken(validToken)).thenReturn(Optional.of(tokenEntity));

        // Act
        logoutService.logout(request, response, authentication);

        // Assert
        assertTrue(tokenEntity.isExpired());
        assertTrue(tokenEntity.isRevoked());
        verify(tokenRepository).save(tokenEntity);
    }

    /**
     * Test case to verify behavior when no Authorization header is present in the request.
     * This test ensures that if there is no Authorization header,
     * no interaction with the token repository occurs,
     * preventing unnecessary operations when logging out without a valid token.
     */
    @Test
    public void logout_NoAuthorizationHeader() {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        logoutService.logout(request, response, authentication);

        // Assert: No interaction with tokenRepository should occur
        verify(tokenRepository, never()).findByToken(any());
    }

    /**
     * Test case to verify behavior when an invalid token format is provided in the Authorization header.
     * This test ensures that if an invalid format is detected,
     * no interaction with the token repository occurs,
     * maintaining robustness against malformed requests during logout attempts.
     */
    @Test
    public void logout_InvalidTokenFormat() {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("InvalidTokenFormat");

        // Act
        logoutService.logout(request, response, authentication);

        // Assert: No interaction with tokenRepository should occur
        verify(tokenRepository, never()).findByToken(any());
    }

    /**
     * Test case to verify behavior when a valid Bearer token does not correspond to any existing tokens in the repository.
     * This test ensures that if no matching token is found,
     * no changes are made to any token entity and no save operation occurs,
     * preventing errors or unintended modifications during logout attempts with non-existent tokens.
     */
    @Test
    public void logout_TokenNotFound() {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        when(tokenRepository.findByToken(validToken)).thenReturn(Optional.empty());

        // Act
        logoutService.logout(request, response, authentication);

        // Assert: No changes to the token entity and no save call should occur
        verify(tokenRepository, never()).save(any());
    }
}