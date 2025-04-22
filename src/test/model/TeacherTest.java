package model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
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
    void testAddCourse_NullIgnored() {
        teacher.addCourse(null);
        assertEquals(0, teacher.getTeachingCourses().size());
    }

    @Test
    void testAddCourse_DuplicateIgnored() {
        teacher.addCourse(course1);
        teacher.addCourse(course1);
        assertEquals(1, teacher.getTeachingCourses().size());
    }

    @Test
    void testRemoveCourse() {
        teacher.addCourse(course1);
        teacher.addCourse(course2);
        teacher.removeCourse(course1);
        assertFalse(teacher.getTeachingCourses().contains(course1));
    }

    @Test
    void testRemoveCourse_NotPresent() {
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

    @Test
    void testGetTeachingCourses_Encapsulation() {
        teacher.addCourse(course1);
        List<Course> copy = teacher.getTeachingCourses();
        copy.clear();
        assertEquals(1, teacher.getTeachingCourses().size());
    }

    @Test
    void testCreateCourse_PersistsToFile() throws Exception {
        Path path = Path.of("courses.txt");
        Files.deleteIfExists(path);
        teacher.createCourse("Biology 101");
        assertTrue(Files.exists(path));
        String content = Files.readString(path);
        assertTrue(content.contains("Biology 101"));
    }

    @Test
    void testEnrollStudent_AddsToCourse() {
        Student student = new Student("Jane", "Smith", "jane_s", "pass");
        teacher.enrollStudent(course1, student);
        assertTrue(course1.getStudents().contains(student));
    }
}
