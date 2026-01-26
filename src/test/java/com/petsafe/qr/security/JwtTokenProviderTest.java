package com.petsafe.qr.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtTokenProvider Tests")
class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    
    private static final String JWT_SECRET = "ThisIsAVeryLongSecretKeyForTestingPurposesItMustBeAtLeast256Bits";
    private static final long JWT_EXPIRATION = 86400000L; // 24 hours

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtSecret", JWT_SECRET);
        ReflectionTestUtils.setField(jwtTokenProvider, "jwtExpiration", JWT_EXPIRATION);
    }

    @Test
    @DisplayName("Should generate token successfully")
    void testGenerateToken_Success() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UserPrincipal userPrincipal = new UserPrincipal(
                userId, "Test User", "test@example.com", "password",
                Collections.emptyList()
        );
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, Collections.emptyList()
        );

        // Act
        String token = jwtTokenProvider.generateToken(authentication);

        // Assert
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token.split("\\.")).hasSize(3); // JWT has 3 parts
    }

    @Test
    @DisplayName("Should extract userId from valid token")
    void testGetUserIdFromToken_Success() {
        // Arrange
        UUID expectedUserId = UUID.randomUUID();
        String token = jwtTokenProvider.generateTokenFromUserId(expectedUserId);

        // Act
        UUID extractedUserId = jwtTokenProvider.getUserIdFromToken(token);

        // Assert
        assertThat(extractedUserId).isEqualTo(expectedUserId);
    }

    @Test
    @DisplayName("Should return true for valid token")
    void testValidateToken_ValidToken() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String token = jwtTokenProvider.generateTokenFromUserId(userId);

        // Act
        boolean isValid = jwtTokenProvider.validateToken(token);

        // Assert
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Should return false for invalid token")
    void testValidateToken_InvalidToken() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act
        boolean isValid = jwtTokenProvider.validateToken(invalidToken);

        // Assert
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should return false for expired token")
    void testValidateToken_ExpiredToken() {
        // Arrange - Set expiration to -1 to create expired token
        JwtTokenProvider expiredTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(expiredTokenProvider, "jwtSecret", JWT_SECRET);
        ReflectionTestUtils.setField(expiredTokenProvider, "jwtExpiration", -1000L); // Already expired
        
        UUID userId = UUID.randomUUID();
        String expiredToken = expiredTokenProvider.generateTokenFromUserId(userId);

        // Act - Validate with normal provider
        boolean isValid = jwtTokenProvider.validateToken(expiredToken);

        // Assert
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should return false for null token")
    void testValidateToken_NullToken() {
        // Act
        boolean isValid = jwtTokenProvider.validateToken(null);

        // Assert
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should return false for empty token")
    void testValidateToken_EmptyToken() {
        // Act
        boolean isValid = jwtTokenProvider.validateToken("");

        // Assert
        assertThat(isValid).isFalse();
    }
}
