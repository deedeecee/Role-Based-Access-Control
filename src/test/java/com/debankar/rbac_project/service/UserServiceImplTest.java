package com.debankar.rbac_project.service;

import com.debankar.rbac_project.dto.UserCreationDTO;
import com.debankar.rbac_project.entity.User;
import com.debankar.rbac_project.entity.token.Token;
import com.debankar.rbac_project.enums.Role;
import com.debankar.rbac_project.mapper.UserMapper;
import com.debankar.rbac_project.repository.TokenRepository;
import com.debankar.rbac_project.repository.UserRepository;
import com.debankar.rbac_project.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private UserCreationDTO userCreationDTO;

    private User user;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userCreationDTO = new UserCreationDTO();
        userCreationDTO.setUsername("testUser");
        userCreationDTO.setEmail("test@example.com");
        userCreationDTO.setPassword("password123");
        userCreationDTO.setRoles(Set.of(Role.USER));

        user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setRoles(Set.of(Role.USER));
    }

    @Test
    public void registerUser_Success() {
        when(userRepository.existsByEmail(userCreationDTO.getEmail())).thenReturn(false);
        when(userMapper.toUser(userCreationDTO)).thenReturn(user);
        when(passwordEncoder.encode(userCreationDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtTokenProvider.generateToken(user.getEmail())).thenReturn("jwtToken");

        User registeredUser = userService.registerUser(userCreationDTO);

        assertNotNull(registeredUser);
        assertEquals("testUser", registeredUser.getUsername());
        verify(userRepository).save(any(User.class));
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    public void registerUser_EmailAlreadyExists() {
        when(userRepository.existsByEmail(userCreationDTO.getEmail())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(userCreationDTO);
        });

        assertEquals("Email already exists!", exception.getMessage());
    }

    @Test
    public void authenticate_Success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtTokenProvider.generateToken(user.getEmail())).thenReturn("jwtToken");

        String jwtToken = userService.authenticate(user.getEmail(), "password123");

        assertEquals("jwtToken", jwtToken);
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    public void authenticate_InvalidCredentials() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.authenticate(user.getEmail(), "wrongPassword");
        });

        assertEquals("Invalid email or password!", exception.getMessage());
    }

    @Test
    public void findByUserId_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.findByUserId(1L);

        assertNotNull(foundUser);
        assertEquals("testUser", foundUser.getUsername());
    }

    @Test
    public void findByUserId_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.findByUserId(1L);
        });

        assertEquals("User not found!", exception.getMessage());
    }

    @Test
    public void findAll_ReturnsUsers() {
        List<User> users = List.of(user);

        when(userRepository.findAll()).thenReturn(users);

        List<User> foundUsers = userService.findAll();

        assertEquals(1, foundUsers.size());
        assertEquals("testUser", foundUsers.get(0).getUsername());
    }
}



