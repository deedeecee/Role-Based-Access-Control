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

public class LogoutServiceTest {

    @InjectMocks
    private LogoutService logoutService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    private final String validToken = "validToken";
    private Token tokenEntity;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        tokenEntity = new Token();
        tokenEntity.setToken(validToken);
        tokenEntity.setExpired(false);
        tokenEntity.setRevoked(false);
    }

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

    @Test
    public void logout_NoAuthorizationHeader() {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        logoutService.logout(request, response, authentication);

        // Assert: No interaction with tokenRepository should occur
        verify(tokenRepository, never()).findByToken(any());
    }

    @Test
    public void logout_InvalidTokenFormat() {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("InvalidTokenFormat");

        // Act
        logoutService.logout(request, response, authentication);

        // Assert: No interaction with tokenRepository should occur
        verify(tokenRepository, never()).findByToken(any());
    }

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