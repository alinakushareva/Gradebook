package org.gradebook.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the SecurityUtil class.
 */
public class SecurityUtilTest {
    
    @Test
    @DisplayName("Test password hashing")
    public void testPasswordHashing() {
        String password = "mySecurePassword123!";
        
        // Hash the password
        String hashedPassword = SecurityUtil.hashPassword(password);
        
        // Check that the hash is not null or empty
        assertNotNull(hashedPassword);
        assertFalse(hashedPassword.isEmpty());
        
        // Verify that the hash is not the same as the original password
        assertNotEquals(password, hashedPassword);
        
        // Hashing the same password should produce the same hash
        String secondHash = SecurityUtil.hashPassword(password);
        assertEquals(hashedPassword, secondHash);
        
        // Different passwords should produce different hashes
        String differentPassword = "anotherPassword456@";
        String differentHash = SecurityUtil.hashPassword(differentPassword);
        assertNotEquals(hashedPassword, differentHash);
    }
    
    @Test
    @DisplayName("Test password validation")
    public void testPasswordValidation() {
        String password = "mySecurePassword123!";
        String hashedPassword = SecurityUtil.hashPassword(password);
        
        // Check correct password
        assertTrue(SecurityUtil.validatePassword(password, hashedPassword));
        
        // Check incorrect password
        assertFalse(SecurityUtil.validatePassword("wrongPassword", hashedPassword));
    }
    
    @ParameterizedTest
    @DisplayName("Test valid usernames")
    @ValueSource(strings = {
        "user123",
        "john_doe",
        "admin_2021",
        "a_1",
        "abcdefghijklmnopqrst" // 20 characters
    })
    public void testValidUsernames(String username) {
        assertTrue(SecurityUtil.isValidUsername(username));
    }
    
    @ParameterizedTest
    @DisplayName("Test invalid usernames")
    @ValueSource(strings = {
        "", // Empty
        "ab", // Too short
        "user-name", // Contains invalid character
        "user.name", // Contains invalid character
        "user name", // Contains space
        "abcdefghijklmnopqrstu" // 21 characters, too long
    })
    public void testInvalidUsernames(String username) {
        assertFalse(SecurityUtil.isValidUsername(username));
    }
    
    @ParameterizedTest
    @DisplayName("Test valid passwords")
    @ValueSource(strings = {
        "Password123!", 
        "SecureP@ss1",
        "Ab3d$Ef6g",
        "Test1234#",
        "Complex!2Password"
    })
    public void testValidPasswords(String password) {
        assertTrue(SecurityUtil.isValidPassword(password));
    }
    
    @ParameterizedTest
    @DisplayName("Test invalid passwords")
    @ValueSource(strings = {
        "", // Empty
        "short1", // Too short
        "password", // No numbers or uppercase or special chars
        "12345678", // No letters or special chars
        "        ", // Only spaces
        "Password", // No numbers or special chars
        "password123", // No uppercase or special chars
        "PASSWORD123", // No lowercase or special chars
        "Password123", // No special chars
        "Pass!word", // No numbers
        "pass!123" // No uppercase
    })
    public void testInvalidPasswords(String password) {
        assertFalse(SecurityUtil.isValidPassword(password));
    }
    
    @Test
    @DisplayName("Test null inputs")
    public void testNullInputs() {
        // Null username
        assertFalse(SecurityUtil.isValidUsername(null));
        
        // Null password
        assertFalse(SecurityUtil.isValidPassword(null));
    }
} 