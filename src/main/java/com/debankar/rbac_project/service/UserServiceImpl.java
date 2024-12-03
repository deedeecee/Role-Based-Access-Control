package com.debankar.rbac_project.service;

import com.debankar.rbac_project.dto.UserCreationDTO;
import com.debankar.rbac_project.entity.User;
import com.debankar.rbac_project.enums.Role;
import com.debankar.rbac_project.enums.TokenType;
import com.debankar.rbac_project.mapper.UserMapper;
import com.debankar.rbac_project.repository.TokenRepository;
import com.debankar.rbac_project.repository.UserRepository;
import com.debankar.rbac_project.security.JwtTokenProvider;
import com.debankar.rbac_project.entity.token.Token;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/*
 * This class provides the implementation of the UserService interface.
 * It contains the business logic for managing users, including registration, authentication, and retrieval operations.
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;

    // Constructor-based dependency injection to ensure all required services are provided.
    public UserServiceImpl(UserRepository userRepository, TokenRepository tokenRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public User registerUser(UserCreationDTO userDTO) {
        // Checking if the email already exists in the repository to prevent duplicate registrations.
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists!");
        }

        // Mapping the incoming DTO to a User entity and encoding the user's password for security.
        User user = userMapper.toUser(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // Setting default role as USER if no roles are specified during registration.
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of(Role.USER));
        }

        User savedUser = userRepository.save(user);

        // Generating a JWT token for the user.
        String jwtToken = jwtTokenProvider.generateToken(user.getEmail());
        saveUserToken(savedUser, jwtToken);

        return user;    // Returns the newly registered user entity.
    }

    @Override
    public String authenticate(String email, String password) {
        // Retrieving the user by email and checking if the provided password matches the stored hash.
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty() || !passwordEncoder.matches(password, userOptional.get().getPassword())) {
            throw new IllegalArgumentException("Invalid email or password!");
        }

        User user = userOptional.get();

        // Generating a JWT token upon successful authentication
        String jwtToken = jwtTokenProvider.generateToken(user.getEmail());
        revokeAllUserTokens(user);      // Revoking previous tokens to prevent reuse after login.
        saveUserToken(user, jwtToken);  // Saving the new token in the repository.

        return jwtToken;    // Returns the newly generated JWT token for authenticated sessions.
    }

    // Creates a new Token entity representing the user's JWT token with relevant details.
    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)     // Indicates that this token is active initially.
                .expired(false)     // Indicates that this token is not expired initially.
                .build();

        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        // Retrieving all valid tokens associated with the specified user from the repository.
        List<Token> validUserTokens = tokenRepository.findAllValidTokensByUserId(user.getId());

        if (validUserTokens.isEmpty()) {
            return;
        }

        validUserTokens.forEach(token -> {
            token.setExpired(true);     // Marks each valid token as expired.
            token.setRevoked(true);     // Marks each valid token as revoked to prevent further use.
        });

        tokenRepository.saveAll(validUserTokens);   // Saves all updated tokens back to the repository in bulk.
    }

    @Override
    public User findByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
