package com.debankar.rbac_project.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * This component is responsible for generating and validating JWT tokens used for authentication.
 * It provides methods to extract information from tokens and check their validity.
 */
@Component
public class JwtTokenProvider {
    // Secret key used for signing JWT tokens, injected from application.yml
    @Value("${my.secret.key}")
    private String secretKey;

    // Generating a signing key from the secret key for token signing and verification.
    private SecretKey getSigningKey() {
        // HMAC SHA algorithm to create a signing key from the secret key bytes.
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /*
     * Extracts all claims(pieces of information about a subject, such as a user, that are encoded in a JSON object)
     * from the provided JWT token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())    // Verify with Signing key
                .build()
                .parseSignedClaims(token)       // Parse signed claims from token
                .getPayload();                  // Retrieve claims payload
    }

    // Returns the subject of the token (username).
    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims != null ? claims.getSubject() : null;
    }

    // Returns the expiration date of the token.
    private Date extractExpiration(String token) {
        Claims claims = extractAllClaims(token);
        return claims != null ? claims.getExpiration() : null;
    }

    // Validates whether a given JWT token is still valid i.e. not expired.
    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    // Returns true if expiration date is before current date(now).
    private boolean isTokenExpired(String token) {
        Date expiration = extractExpiration(token);
        return expiration != null && expiration.before(new Date());
    }

    // Creates and returns a new token with specified claims and subject(username).
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 60 * 60 * 1000); // Token expires in 60 minutes

        return Jwts.builder()
                .claims(claims)             // Set claims in the token
                .subject(subject)           // Set subject (username)
                .issuedAt(now)              // Set issued timestamp
                .expiration(expiryDate)     // Set expiry timestamp
                .signWith(getSigningKey())  // Sign the token with signing key
                .compact();                 // Build and return the compacted JWT string
    }
}