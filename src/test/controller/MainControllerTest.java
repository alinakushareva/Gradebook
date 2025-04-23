package controller;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.SecurityUtil;

import static org.junit.jupiter.api.Assertions.*;

class MainControllerTest {

    private GradebookModel model;
    private MainController controller;
    private Student student;
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        model = new GradebookModel();
        student = new Student("Alina", "Kushareva", "alina_k", SecurityUtil.hashPassword("1234"));
        teacher = new Teacher("jdoe", "John", "Doe", SecurityUtil.hashPassword("abcd"));

        model.addStudent(student);
        model.addTeacher(teacher);

        controller = new MainController(model, null); // null for view since we're not testing it
    }

    @Test
    void testAuthenticate_ValidStudent() {
        boolean result = controller.authenticate("alina_k", "1234");
        assertTrue(result);
        assertEquals("alina_k", controller.getCurrentUser().getUsername());
    }

    @Test
    void testAuthenticate_InvalidStudentPassword() {
        boolean result = controller.authenticate("alina_k", "wrong");
        assertFalse(result);
        assertNull(controller.getCurrentUser());
    }

    @Test
    void testAuthenticate_ValidTeacher() {
        boolean result = controller.authenticate("jdoe", "abcd");
        assertTrue(result);
        assertEquals("jdoe", controller.getCurrentUser().getUsername());
    }

    @Test
    void testAuthenticate_InvalidTeacherPassword() {
        boolean result = controller.authenticate("jdoe", "wrong");
        assertFalse(result);
        assertNull(controller.getCurrentUser());
    }

    @Test
    void testAuthenticate_NonexistentUser() {
        boolean result = controller.authenticate("ghost", "none");
        assertFalse(result);
        assertNull(controller.getCurrentUser());
    }

    @Test
    void testGetCurrentUser_BeforeAndAfterLogin() {
        assertNull(controller.getCurrentUser());
        controller.authenticate("jdoe", "abcd");
        assertNotNull(controller.getCurrentUser());
        assertEquals("jdoe", controller.getCurrentUser().getUsername());
    }
}