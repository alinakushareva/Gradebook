package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class UserTest {

    private static final String USERNAME = "AlinaKushaeva";
    private static final String FIRST_NAME = "Alina";
    private static final String LAST_NAME = "Kushaeva";
    private static final String PASSWORD_HASH = "hashedPass123";
    private static final String ROLE = "student";

    private User user;

    // Dummy subclass to allow instantiating the abstract User
    private static class DummyUser extends User {
        public DummyUser(String username, String firstName, String lastName, String passwordHash, String role) {
            super(username, firstName, lastName, passwordHash, role);
        }

        @Override
        public String toFileString() {
            return getUsername() + "," + getFullName() + "," + getPasswordHash() + "," + getRole();
        }
    }

    @BeforeEach
    void setUp() {
        user = new DummyUser(USERNAME, FIRST_NAME, LAST_NAME, PASSWORD_HASH, ROLE);
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

    @Test
    void getFirstName_ReturnsFirstName() {
        assertEquals("Alina", user.getFirstName());
    }

    @Test
    void getLastName_ReturnsLastName() {
        assertEquals("Kushaeva", user.getLastName());
    }

    // --- getCourses() ---
    @Test
    void getCourses_InitiallyReturnsEmptyList() {
        assertTrue(user.getCourses().isEmpty());
    }

    @Test
    void getCourses_ReturnsDefensiveCopy() {
        List<Course> firstCall = user.getCourses();
        firstCall.add(null); // should not affect internal list
        assertTrue(user.getCourses().isEmpty());
    }

    // --- addCourse() ---
    @Test
    void addCourse_RejectsNullCourse() {
        user.addCourse(null);
        assertTrue(user.getCourses().isEmpty());
    }

    @Test
    void addCourse_AddsCourseIfNotDuplicate() {
        Course course = new Course("CSC335");
        user.addCourse(course);
        assertEquals(1, user.getCourses().size());
        assertTrue(user.getCourses().contains(course));
    }

    @Test
    void addCourse_IgnoresDuplicate() {
        Course course = new Course("CSC335");
        user.addCourse(course);
        user.addCourse(course); // try duplicate
        assertEquals(1, user.getCourses().size());
    }

    // --- toFileString() ---
    @Test
    void toFileString_FormatsCorrectly() {
        String expected = "AlinaKushaeva,Alina Kushaeva,hashedPass123,student";
        assertEquals(expected, user.toFileString());
    }
}
