package test.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {
    private Teacher teacher;
    private Course course1;
    private Course course2;

    @BeforeEach
    void setUp() {
        teacher = new Teacher("john_doe", "John", "Doe", "hashedPassword");
        course1 = new Course("Math 101");
        course2 = new Course("Physics 201");
    }

    @Test
    void testAddCourse() {
        teacher.addCourse(course1);
        assertTrue(teacher.getTeachingCourses().contains(course1));
    }

    @Test
    void testRemoveCourse() {
        teacher.addCourse(course1);
        teacher.addCourse(course2);
        teacher.removeCourse(course1);
        assertFalse(teacher.getTeachingCourses().contains(course1));
    }

    @Test
    void testGetTeachingCourses() {
        teacher.addCourse(course1);
        teacher.addCourse(course2);
        List<Course> courses = teacher.getTeachingCourses();
        assertEquals(2, courses.size());
        assertTrue(courses.contains(course1));
        assertTrue(courses.contains(course2));
    }
}

