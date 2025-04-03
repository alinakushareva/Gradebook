package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class UserTest {
    // Test data matching your User class
    private static final String USERNAME = "AlinaKushaeva";
    private static final String FIRST_NAME = "Alina";
    private static final String LAST_NAME = "Kushaeva";
    private static final String PASSWORD_HASH = "hashedPass123";
    private static final String ROLE = "student";
    
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(USERNAME, FIRST_NAME, LAST_NAME, PASSWORD_HASH, ROLE) {};
    }

    // --- Constructor Validation ---
    @Test 
    void constructor_SetsUsername() {
        assertEquals(USERNAME, user.getUsername());
    }

    @Test
    void constructor_SetsPasswordHash() {
        assertEquals(PASSWORD_HASH, user.getPasswordHash());
    }

    @Test
    void constructor_SetsRole() {
        assertEquals(ROLE, user.getRole());
    }

    // --- getFullName() ---
    @Test
    void getFullName_ReturnsFirstNameSpaceLastName() {
        assertEquals("Alina Kushaeva", user.getFullName());
    }

    // --- getCourses() ---
    @Test
    void getCourses_InitiallyReturnsEmptyList() {
        assertTrue(user.getCourses().isEmpty());
    }

    @Test
    void getCourses_ReturnsDefensiveCopy() {
        user.getCourses().add(null); 
        assertTrue(user.getCourses().isEmpty()); // Original list unaffected
    }

    // --- addCourse() ---
    @Test
    void addCourse_RejectsNullCourse() {
        user.addCourse(null);
        assertTrue(user.getCourses().isEmpty());
    }
  
}