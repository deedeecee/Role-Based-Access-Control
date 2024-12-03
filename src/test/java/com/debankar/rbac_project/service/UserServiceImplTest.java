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

/**
 * This class contains unit tests for the UserServiceImpl class.
 */
public class UserServiceImplTest {

    @InjectMocks    // Creates an instance of UserServiceImpl and injects mocked dependencies into it.
    private UserServiceImpl userService;

    @Mock   // Creates a mock instance of UserRepository for testing.
    private UserRepository userRepository;

    @Mock   // Creates a mock instance of TokenRepository for testing.
    private TokenRepository tokenRepository;

    @Mock   // Creates a mock instance of PasswordEncoder for testing.
    private PasswordEncoder passwordEncoder;

    @Mock   // Creates a mock instance of UserMapper for testing.
    private UserMapper userMapper;

    @Mock   // Creates a mock instance of JwtTokenProvider for testing.
    private JwtTokenProvider jwtTokenProvider;

    private UserCreationDTO userCreationDTO;

    private User user;

    /** Setting up the necessary context before each test case. */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);     // This initializes mocks annotated with @Mock

        // Setting up test data for user creation.
        userCreationDTO = new UserCreationDTO();
        userCreationDTO.setUsername("testUser");
        userCreationDTO.setEmail("test@example.com");
        userCreationDTO.setPassword("password123");
        userCreationDTO.setRoles(Set.of(Role.USER));

        // Setting up a corresponding User entity for testing.
        user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setRoles(Set.of(Role.USER));
    }

    /**
     * Test case to verify successful registration of a new user.
     * This test checks that when a valid UserCreationDTO is provided.
     * The service correctly processes the registration by ensuring:
     * 1. The email does not already exist in the repository,
     * 2. The DTO is converted to a User entity,
     * 3. The password is encoded properly,
     * 4. The new user is saved in the repository, and a JWT token is generated.
     */
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

    /**
     * Test case to verify behavior when attempting to register with an existing email.
     * This test ensures that if an email already exists in the repository, an IllegalArgumentException is thrown
     * with the appropriate message, preventing duplicate registrations and maintaining data integrity.
     */
    @Test
    public void registerUser_EmailAlreadyExists() {
        when(userRepository.existsByEmail(userCreationDTO.getEmail())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(userCreationDTO);
        });

        assertEquals("Email already exists!", exception.getMessage());
    }

    /**
     * Test case to verify successful authentication of a user.
     * This test checks that when valid credentials are provided,
     * the service retrieves the corresponding user, verifies the password,
     * and generates a JWT token. It ensures that authentication works as intended
     * and that a token is created for valid users.
     */
    @Test
    public void authenticate_Success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtTokenProvider.generateToken(user.getEmail())).thenReturn("jwtToken");

        String jwtToken = userService.authenticate(user.getEmail(), "password123");

        assertEquals("jwtToken", jwtToken);
        verify(tokenRepository).save(any(Token.class));
    }

    /**
     * Test case to verify behavior when authentication fails due to invalid credentials.
     * This test ensures that if no user is found with the provided email or if
     * the password does not match, an IllegalArgumentException is thrown
     * with an appropriate message, preventing unauthorized access attempts.
     */
    @Test
    public void authenticate_InvalidCredentials() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.authenticate(user.getEmail(), "wrongPassword");
        });

        assertEquals("Invalid email or password!", exception.getMessage());
    }

    /**
     * Test case to verify successful retrieval of a user by their ID.
     * This test checks that when a valid ID is provided,
     * the service correctly retrieves and returns the corresponding User entity,
     * ensuring that users can be found by their unique identifiers as expected.
     */
    @Test
    public void findByUserId_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.findByUserId(1L);

        assertNotNull(foundUser);
        assertEquals("testUser", foundUser.getUsername());
    }

    /**
     * Test case to verify behavior when attempting to find a non-existent user by ID.
     * This test ensures that if no user is found with the provided ID,
     * an IllegalArgumentException is thrown with an appropriate message,
     * maintaining robustness in handling invalid requests for users by ID.
     */
    @Test
    public void findByUserId_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.findByUserId(1L);
        });

        assertEquals("User not found!", exception.getMessage());
    }

    /**
     * Test case to verify retrieval of all users from the repository.
     * This test checks that when there are users in the system,
     * they are correctly retrieved and returned as a list, ensuring
     * that all registered users can be accessed as expected by administrators or other services.
     */
    @Test
    public void findAll_ReturnsUsers() {
        List<User> users = List.of(user);

        when(userRepository.findAll()).thenReturn(users);

        List<User> foundUsers = userService.findAll();

        assertEquals(1, foundUsers.size());
        assertEquals("testUser", foundUsers.get(0).getUsername());
    }
}



