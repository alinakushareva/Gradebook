package util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class SecurityUtilTest {

    @Test
    void hashPassword_ProducesConsistentOutput() {
        String hash1 = SecurityUtil.hashPassword("AlinaKushareva123!");
        String hash2 = SecurityUtil.hashPassword("AlinaKushareva123!");
        assertEquals(hash1, hash2);
    }

    @Test
    void hashPassword_ProducesDifferentHashesForDifferentInputs() {
        String hash1 = SecurityUtil.hashPassword("Password1!");
        String hash2 = SecurityUtil.hashPassword("Password2@");
        assertNotEquals(hash1, hash2);
    }

    @Test
    void checkPassword_ReturnsTrueForCorrectPassword() {
        String hash = SecurityUtil.hashPassword("JakeSmith456@");
        assertTrue(SecurityUtil.checkPassword("JakeSmith456@", hash));
    }

    @Test
    void checkPassword_ReturnsFalseForWrongPassword() {
        String hash = SecurityUtil.hashPassword("correctPass");
        assertFalse(SecurityUtil.checkPassword("wrongPass", hash));
    }

    @Test
    void isValidPassword_AcceptsValidPassword() {
        assertTrue(SecurityUtil.isValidPassword("ValidPass123!"));
    }

    @Test
    void isValidPassword_RejectsNullPassword() {
        assertFalse(SecurityUtil.isValidPassword(null));
    }

    @Test
    void isValidPassword_RejectsShortPassword() {
        assertFalse(SecurityUtil.isValidPassword("A1!"));
    }

    @Test
    void isValidPassword_RejectsMissingNumber() {
        assertFalse(SecurityUtil.isValidPassword("MissingNum!"));
    }

    @Test
    void isValidPassword_RejectsMissingUppercase() {
        assertFalse(SecurityUtil.isValidPassword("missingupper1!"));
    }

    @Test
    void isValidPassword_RejectsMissingLowercase() {
        assertFalse(SecurityUtil.isValidPassword("MISSINGLOWER1!"));
    }

    @Test
    void isValidPassword_RejectsMissingSpecialChar() {
        assertFalse(SecurityUtil.isValidPassword("MissingSpecial1"));
    }

    @Test
    void isValidPassword_RejectsWhitespace() {
        assertFalse(SecurityUtil.isValidPassword("Pass 123!"));
    }

    @Test
    void isValidUsername_AcceptsAlinaKushareva() {
        assertTrue(SecurityUtil.isValidUsername("alina_k"));
    }

    @Test
    void isValidUsername_AcceptsJakeSmith() {
        assertTrue(SecurityUtil.isValidUsername("jake_s"));
    }

    @Test
    void isValidUsername_RejectsNullUsername() {
        assertFalse(SecurityUtil.isValidUsername(null));
    }

    @Test
    void isValidUsername_RejectsShortUsername() {
        assertFalse(SecurityUtil.isValidUsername("ali"));
    }

    @Test
    void isValidUsername_RejectsLongUsername() {
        assertFalse(SecurityUtil.isValidUsername("this_username_is_way_too_long"));
    }

    @Test
    void isValidUsername_RejectsSpaces() {
        assertFalse(SecurityUtil.isValidUsername("alina k"));
    }

    @Test
    void isValidUsername_RejectsSpecialChars() {
        assertFalse(SecurityUtil.isValidUsername("alina@k"));
    }

    @Test
    void getPasswordRegex_ReturnsExpectedPattern() {
        String regex = SecurityUtil.getPasswordRegex();
        assertNotNull(regex);
        assertTrue(regex.contains("(?=.*[0-9])"));  // Number requirement
        assertTrue(regex.contains("(?=.*[a-z])"));  // Lowercase requirement
        assertTrue(regex.contains("(?=.*[A-Z])"));  // Uppercase requirement
        assertTrue(regex.contains("(?=.*[!@#$%^&*])"));  // Special char requirement
        assertTrue(regex.contains("\\S+"));  // No whitespace requirement
    }
}