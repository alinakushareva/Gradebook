package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GradebookModelTest {

    private GradebookModel model;
    private Student student;
    private Teacher teacher;
    private Course course;

    @BeforeEach
    void setUp() {
        model = new GradebookModel();
        student = new Student("Alina", "Kushareva", "alina_k", "pass");
        teacher = new Teacher("john_doe", "John", "Doe", "pass");
        course = new Course("CSC335");
    }

    @Test
    void testAddAndGetStudent() {
        model.addStudent(student);
        Student result = model.getStudentByUsername("alina_k");
        assertEquals(student, result);
    }

    @Test
    void testStudentExists() {
        model.addStudent(student);
        assertTrue(model.studentExists("alina_k"));
    }

    @Test
    void testStudentDoesNotExist() {
        assertFalse(model.studentExists("ghost"));
    }

    @Test
    void testAddAndGetTeacher() {
        model.addTeacher(teacher);
        Teacher result = model.getTeacherByUsername("john_doe");
        assertEquals(teacher, result);
    }

    @Test
    void testTeacherExists() {
        model.addTeacher(teacher);
        assertTrue(model.teacherExists("john_doe"));
    }

    @Test
    void testTeacherDoesNotExist() {
        assertFalse(model.teacherExists("unknown_teacher"));
    }

    @Test
    void testAddAndGetCourse() {
        model.addCourse(course);
        Course result = model.getCourseByName("CSC335");
        assertEquals(course, result);
    }

    @Test
    void testGetAllCourses() {
        Course course2 = new Course("MATH122");
        model.addCourse(course);
        model.addCourse(course2);
        List<Course> allCourses = model.getAllCourses();
        assertTrue(allCourses.contains(course));
        assertTrue(allCourses.contains(course2));
        assertEquals(2, allCourses.size());
    }
}
