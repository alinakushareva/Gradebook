/**
 * Project Name: Gradebook
 * File Name: SecurityUtil.java
 * Course: CSC 335 Spring 2025
 * Purpose: Provides security-related functions including password hashing, validation,
 *          and username verification. Ensures secure storage and authentication.
 */
package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

public class SecurityUtil {
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final int MIN_PASSWORD_LENGTH = 8;
    // Password pattern requires: 1 digit, 1 lowercase, 1 uppercase, 1 special char, no spaces
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
    	    "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])(?=\\S+$).{" + MIN_PASSWORD_LENGTH + ",}$"
    	);
    
    /**
     * Hashes a password using SHA-256 algorithm
     * @param password Plaintext password to hash
     * @return Hexadecimal string representation of the hash
     * @throws RuntimeException if hashing algorithm is unavailable
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            byte[] hashBytes = digest.digest(password.getBytes());
            
            // Convert byte array to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0'); // Pad single digits
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash password", e);
        }
    }

    /**
     * Verifies a password against a stored hash
     * @param inputPassword Password to verify
     * @param storedHash Stored hash to compare against
     * @return true if password matches hash, false otherwise
     */
    public static boolean checkPassword(String inputPassword, String storedHash) {
        return hashPassword(inputPassword).equals(storedHash);
    }
    
    /**
     * Validates password meets complexity requirements
     * @param password Password to validate
     * @return true if password meets all requirements
     */
    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }
    
    /**
     * Validates username format (4-20 alphanumeric + underscore)
     * @param username Username to validate
     * @return true if username meets format requirements
     */
    public static boolean isValidUsername(String username) {
        return username != null && username.matches("^[a-zA-Z0-9_]{4,20}$");
    }

    /**
     * Returns the password regex pattern (for testing/debugging)
     * @return String representation of password pattern
     */    static String getPasswordRegex() {
        return PASSWORD_PATTERN.pattern();
    }
}