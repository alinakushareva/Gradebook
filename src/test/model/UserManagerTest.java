package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import util.SecurityUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserManagerTest {

    private Path userFile;

    @BeforeEach
    void setup(@TempDir Path tempDir) throws IOException {
        userFile = tempDir.resolve("users.txt");
        Files.write(userFile, List.of());
    }

    @Test
    void testRegisterAndFindUser() {
        UserManager um = new UserManager(userFile.toString());
        um.registerUser("john123", "John", "Doe", "StrongP@ss1", "student");
        User user = um.findUserByUsername("john123");
        assertNotNull(user);
        assertEquals("john123", user.getUsername());
    }

    @Test
    void testRegisterUser_DuplicateUsernameThrows() {
        UserManager um = new UserManager(userFile.toString());
        um.registerUser("anna123", "Anna", "Smith", "Passw0rd!", "student");
        assertThrows(IllegalArgumentException.class, () ->
            um.registerUser("anna123", "Anna", "Smith", "Passw0rd!", "student")
        );
    }

    @Test
    void testRegisterUser_InvalidUsernameThrows() {
        UserManager um = new UserManager(userFile.toString());
        assertThrows(IllegalArgumentException.class, () ->
            um.registerUser("a", "Test", "User", "Passw0rd!", "student")
        );
    }

    @Test
    void testRegisterUser_InvalidPasswordThrows() {
        UserManager um = new UserManager(userFile.toString());
        assertThrows(IllegalArgumentException.class, () ->
            um.registerUser("validUser", "Test", "User", "badpass", "student")
        );
    }

    @Test
    void testRegisterUser_InvalidRoleThrows() {
        UserManager um = new UserManager(userFile.toString());
        assertThrows(IllegalArgumentException.class, () ->
            um.registerUser("newuser", "Test", "User", "GoodP@ss1", "admin")
        );
    }

    @Test
    void testLoginSuccess() {
        UserManager um = new UserManager(userFile.toString());
        um.registerUser("bob321", "Bob", "Lee", "StrongP@ss1", "teacher");
        User user = um.login("bob321", "StrongP@ss1");
        assertNotNull(user);
    }

    @Test
    void testLoginFailure_WrongPassword() {
        UserManager um = new UserManager(userFile.toString());
        um.registerUser("bob321", "Bob", "Lee", "StrongP@ss1", "teacher");
        User user = um.login("bob321", "WrongPass");
        assertNull(user);
    }

    @Test
    void testLoginFailure_UserNotFound() {
        UserManager um = new UserManager(userFile.toString());
        User user = um.login("ghost", "whatever");
        assertNull(user);
    }


    @Test
    void testAddAndGetAllCourses() {
        UserManager um = new UserManager(userFile.toString());
        Course c1 = new Course("MATH101");
        Course c2 = new Course("PHYS202");
        um.addCourse(c1);
        um.addCourse(c2);
        List<Course> courses = um.getAllCourses();
        assertTrue(courses.contains(c1));
        assertTrue(courses.contains(c2));
    }
    
    @Test
    void testGetAllUsers_ReturnsCopyOfUsersList() {
        UserManager um = new UserManager(userFile.toString());
        um.registerUser("user1", "First", "One", "StrongP@ss1", "student");
        um.registerUser("user2", "Second", "Two", "StrongP@ss2", "teacher");

        List<User> allUsers = um.getAllUsers();

        assertEquals(2, allUsers.size());
        assertTrue(allUsers.stream().anyMatch(u -> u.getUsername().equals("user1")));
        assertTrue(allUsers.stream().anyMatch(u -> u.getUsername().equals("user2")));
    }
    
    @Test
    void testLoadUsersFromFile_IOExceptionHandledGracefully() {
        String invalidPath = "/definitely/does/not/exist/users.txt";
        UserManager um = new UserManager(invalidPath); 

        assertNotNull(um.getAllUsers());
        assertEquals(0, um.getAllUsers().size());
    }



}