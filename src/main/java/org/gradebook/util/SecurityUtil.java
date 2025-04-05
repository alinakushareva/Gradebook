package org.gradebook.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Utility class for security operations such as password hashing and validation.
 */
public class SecurityUtil {
    
    /**
     * Hashes a password using SHA-256 algorithm.
     *
     * @param password the plain text password
     * @return the hashed password, or the original if hashing fails
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Error hashing password: " + e.getMessage());
            return password; // Return unhashed as fallback (not secure but prevents system failure)
        }
    }
    
    /**
     * Checks if a plain text password matches a stored hash.
     *
     * @param password    the plain text password
     * @param storedHash  the stored hash
     * @return true if the password matches, false otherwise
     */
    public static boolean validatePassword(String password, String storedHash) {
        String hashedInput = hashPassword(password);
        return hashedInput.equals(storedHash);
    }
    
    /**
     * Validates that a username follows the required format.
     * Requirements: 3-20 characters, alphanumeric or underscore.
     *
     * @param username the username to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        
        // 3-20 characters, alphanumeric or underscore
        return username.matches("^[a-zA-Z0-9_]{3,20}$");
    }
    
    /**
     * Validates that a password meets minimum security requirements.
     * Requirements: 
     * - At least 8 characters
     * - At least one uppercase letter
     * - At least one lowercase letter
     * - At least one number
     * - At least one special character
     *
     * @param password the password to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        
        // At least 8 characters
        if (password.length() < 8) {
            return false;
        }
        
        // At least one uppercase letter
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        
        // At least one lowercase letter
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
        
        // At least one number
        if (!password.matches(".*[0-9].*")) {
            return false;
        }
        
        // At least one special character
        if (!password.matches(".*[^a-zA-Z0-9].*")) {
            return false;
        }
        
        return true;
    }
} 